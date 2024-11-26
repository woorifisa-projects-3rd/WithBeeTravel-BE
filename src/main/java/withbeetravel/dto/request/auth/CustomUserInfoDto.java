package withbeetravel.dto.request.auth;

import lombok.Builder;
import lombok.Getter;
import withbeetravel.domain.RoleType;
import withbeetravel.domain.User;

// 로직 내부에서 인증 유저 정보를 저장해둘 dto
@Getter
public class CustomUserInfoDto {
    private Long id;
    private String email;
    private String password;
    private String name;
    private RoleType role;

    @Builder
    public CustomUserInfoDto(Long id,
                             String email,
                             String password,
                             String name,
                             RoleType role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public static CustomUserInfoDto from(User user) {
        return CustomUserInfoDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .role(user.getRoleType())
                .build();
    }
}
