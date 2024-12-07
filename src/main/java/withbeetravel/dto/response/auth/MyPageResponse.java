package withbeetravel.dto.response.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import withbeetravel.domain.User;

@Data
@Builder
@Schema(description = "MyPage 초기 정보 Response DTO")
public class MyPageResponse {

    @Schema(
            description = "프로필 이미지 번호",
            example = "1"
    )
    private int profileImage;

    @Schema(
            description = "회원 이름",
            example = "홍길동"
    )
    private String username;

    @Schema(
            description = "계좌 종류",
            example = "WON 통장"
    )
    private String accountProduct;

    @Schema(
            description = "계좌 번호",
            example = "123456789012"
    )
    private String accountNumber;

    public static MyPageResponse from(User user) {
        return MyPageResponse.builder()
                .profileImage(user.getProfileImage())
                .username(user.getName())
                .accountProduct(
                        user.getConnectedAccount() != null
                                ? user.getConnectedAccount().getProduct().name()
                                : null)
                .accountNumber(user.getConnectedAccount() != null
                                ? user.getConnectedAccount().getAccountNumber()
                                : null)
                .build();
    }
}
