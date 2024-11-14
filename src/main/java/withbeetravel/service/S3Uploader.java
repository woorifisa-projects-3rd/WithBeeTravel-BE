package withbeetravel.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

@Service
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private final String bucket;

    @Value("${cloud.aws.s3.bucket.domain}")
    private String bucketDomain;

    public S3Uploader(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    // 이미지 저장
    public String upload(MultipartFile file, String dirName) throws IOException {

        // 파일의 원래 이름에서 공백을 제거
        String originalFileName = file.getOriginalFilename().replaceAll("\\s", "_");

        // 유니크한 파일명을 만들기 위해 UUID를 파일명에 추가
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName;

        // 디렉토리 위치와 파일명 합치기
        String fileName = dirName + "/" + uniqueFileName;

        // MultipartFile -> File
        File uploadFile =convert(file, uniqueFileName);

        // S3에 이미지 업로드
        String uploadImageUrl = putS3(uploadFile, fileName);

        // 임시 파일 삭제
        uploadFile.delete();

        // S3에 저장된 이미지의 URL 리턴
        return uploadImageUrl;
    }

    // 이미지 삭제
    public void delete(String fileName) {
        if(fileName.startsWith(bucketDomain)) {
            amazonS3.deleteObject(bucket, fileName.substring(bucketDomain.length()));
        }
    }

    // 이미지 수정
    public String update(MultipartFile newFile, String oldFileName, String dirName) throws IOException {
        // 기존 파일 삭제
        delete(oldFileName);
        // 새 파일 업로드
        return upload(newFile, dirName);
    }

    // MultipartFile -> File
    private File convert(MultipartFile file, String uniqueFileName) throws IOException {

        File convertFile = new File(uniqueFileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                System.err.println("파일 변환 중 오류 발생: " +  e.getMessage());
                throw e;
            }
            return convertFile;
        }
        throw new IllegalArgumentException("파일 변환에 실패했습니다. " + file.getOriginalFilename());
    }

    // S3에 이미지 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
