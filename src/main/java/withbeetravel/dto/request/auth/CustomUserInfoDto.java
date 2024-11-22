package withbeetravel.dto.request.auth;

import lombok.Getter;

// 로직 내부에서 인증 유저 정보를 저장해둘 dto
@Getter
public class CustomUserInfoDto {
    private Long userId;
    private String email;
    private String password;
    private String name;
    private RoleType role;

    public CustomUserInfoDto(Long userId,
                             String email,
                             String password,
                             String name,
                             RoleType role) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}
