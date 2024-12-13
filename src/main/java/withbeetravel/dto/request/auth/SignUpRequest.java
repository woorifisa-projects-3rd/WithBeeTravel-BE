package withbeetravel.dto.request.auth;

import jakarta.validation.constraints.*;
import lombok.Getter;

// 회원가입 DTO
@Getter
public class SignUpRequest {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{10,}$",
            message = "비밀번호는 영어, 숫자, 특수문자를 포함해 최소 10자 이상만 가능합니다.")
    private String password;

    @NotBlank(message = "핀번호는 필수 입력 값입니다.")
    private String pinNumber;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    public SignUpRequest(String email, String password, String pinNumber, String name) {
        this.email = email;
        this.password = password;
        this.pinNumber = pinNumber;
        this.name = name;
    }
}
