package withbeetravel.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
    // file := s3에 저장할 이미지 파일
    // dirName := 이미지 파일을 저장할 s3 디렉토리
    public String upload(MultipartFile file, String dirName) throws IOException {

        // 파일의 원래 이름에서 공백을 제거
        String originalFileName = file.getOriginalFilename().replaceAll("\\s", "_");

        // 유니크한 파일명을 만들기 위해 UUID를 파일명에 추가
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName;

        // 디렉토리 위치와 파일명 합치기
        String fileName = dirName + "/" + uniqueFileName;

        // S3에 이미지 업로드
        String contentType = file.getContentType();
        if(contentType == null) contentType = "application/octet-stream";
        String uploadImageUrl = putS3(file.getInputStream(), fileName, file.getSize(), contentType);

        // S3에 저장된 이미지의 URL 리턴
        return uploadImageUrl;
    }

    // 이미지 삭제
    // filName := 삭제할 이미지명(URL 형식)
    public void delete(String fileName) {
        if(fileName.startsWith(bucketDomain)) {
            amazonS3.deleteObject(bucket, fileName.substring(bucketDomain.length()));
        }
    }

    // 이미지 수정
    // newFile := 새로 저장할 이미지 파일
    // oldFileName := 기존에 저장되어 있던 이미지명(URL 형식)
    // dirName := 이미지 파일을 저장할 s3 디렉토리
    public String update(MultipartFile newFile, String oldFileName, String dirName) throws IOException {
        // 기존 파일 삭제
        delete(oldFileName);
        // 새 파일 업로드
        return upload(newFile, dirName);
    }

    // S3에 이미지 업로드
    private String putS3(InputStream inputStream, String fileName, long contentLength, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        metadata.setContentType(contentType); // Content-Type 설정

        amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
