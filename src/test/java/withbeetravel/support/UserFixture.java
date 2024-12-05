package withbeetravel.support;

import withbeetravel.domain.Account;
import withbeetravel.domain.RoleType;
import withbeetravel.domain.User;

public class UserFixture {

    private Long id;
    private Account wibeeCardAccount;
    private Account connectedAccount;
    private String email = "1234@naver.com";
    private String password = "password123!";
    private String pinNumber = "123456";
    private String name = "홍길동";
    private int profileImage = 1;
    private int failedPinCount = 0;
    private boolean pinLocked = false;
    private RoleType roleType = RoleType.USER;

    public static UserFixture builder() {
        return new UserFixture();
    }

    public UserFixture id(Long id) {
        this.id = id;
        return this;
    }

    public UserFixture wibeeCardAccount(Account wibeeCardAccount) {
        this.wibeeCardAccount = wibeeCardAccount;
        return this;
    }

    public UserFixture connectedAccount(Account connectedAccount) {
        this.connectedAccount = connectedAccount;
        return this;
    }

    public UserFixture email(String email) {
        this.email = email;
        return this;
    }

    public UserFixture password(String password) {
        this.password = password;
        return this;
    }

    public UserFixture pinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
        return this;
    }

    public UserFixture name(String name) {
        this.name = name;
        return this;
    }

    public UserFixture profileImage(int profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public UserFixture failedPinCount(int failedPinCount) {
        this.failedPinCount = failedPinCount;
        return this;
    }

    public UserFixture pinLocked(boolean pinLocked) {
        this.pinLocked = pinLocked;
        return this;
    }

    public UserFixture roleType(RoleType roleType) {
        this.roleType = roleType;
        return this;
    }

    public User build() {
        return User.builder()
                .id(id)
                .wibeeCardAccount(wibeeCardAccount)
                .connectedAccount(connectedAccount)
                .email(email)
                .password(password)
                .pinNumber(pinNumber)
                .name(name)
                .profileImage(profileImage)
                .failedPinCount(failedPinCount)
                .pinLocked(pinLocked)
                .roleType(roleType)
                .build();
    }


}
