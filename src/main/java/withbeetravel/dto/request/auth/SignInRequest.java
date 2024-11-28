package withbeetravel.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SignInRequest {

    @NotNull(message = "이메일 입력은 필수입니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotNull(message = "비밀번호 입력은 필수입니다.")
    private String password;

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
