package withbeetravel;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import withbeetravel.domain.*;
import withbeetravel.repository.*;

import static withbeetravel.domain.RoleType.ADMIN;
import static withbeetravel.domain.RoleType.USER;

//@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        // User 더미 데이터 생성
        User user0 = User.builder()
                .email("admin@admin")
                .password(BCrypt.hashpw("password123!", BCrypt.gensalt()))
                .pinNumber("123456")
                .name("관리자")
                .roleType(ADMIN)
                .pinLocked(false)
                .failedPinCount(0)
                .profileImage(1)
                .build();

        User user1 = User.builder()
                .email("1@naver.com")
                .password(BCrypt.hashpw("password123!", BCrypt.gensalt()))
                .pinNumber("123456")
                .name("공소연")
                .roleType(USER)
                .pinLocked(false)
                .failedPinCount(0)
                .profileImage(1)
                .build();

        User user2 = User.builder()
                .email("2@naver.com")
                .password(BCrypt.hashpw("password123!", BCrypt.gensalt()))
                .pinNumber("123456")
                .name("공예진")
                .roleType(USER)
                .pinLocked(false)
                .failedPinCount(0)
                .profileImage(2)
                .build();

        User user3 = User.builder()
                .email("3@naver.com")
                .password(BCrypt.hashpw("password123!", BCrypt.gensalt()))
                .pinNumber("123456")
                .name("김호철")
                .roleType(USER)
                .pinLocked(false)
                .failedPinCount(0)
                .profileImage(3)
                .build();

        User user4 = User.builder()
                .email("4@naver.com")
                .password(BCrypt.hashpw("password123!", BCrypt.gensalt()))
                .pinNumber("123456")
                .name("이도이")
                .roleType(USER)
                .pinLocked(false)
                .failedPinCount(0)
                .profileImage(4)
                .build();

        User user5 = User.builder()
                .email("5@naver.com")
                .password(BCrypt.hashpw("password123!", BCrypt.gensalt()))
                .pinNumber("123456")
                .name("유승아")
                .roleType(USER)
                .pinLocked(false)
                .failedPinCount(0)
                .profileImage(5)
                .build();

        userRepository.save(user0);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);

        // Account 더미 데이터 생성
        Account account0 = Account.builder()
                .user(user0)
                .accountNumber("0000000000000")
                .balance(100000000)
                .product(Product.WON통장)
                .isConnectedWibeeCard(false)
                .build();

        Account account1 = Account.builder()
                .user(user1)
                .accountNumber("1111111111111")
                .balance(300000)
                .product(Product.WON통장)
                .isConnectedWibeeCard(true)
                .build();

        Account account2 = Account.builder()
                .user(user2)
                .accountNumber("2222222222222")
                .balance(400000)
                .product(Product.WON통장)
                .isConnectedWibeeCard(true)
                .build();

        Account account3 = Account.builder()
                .user(user3)
                .accountNumber("3333333333333")
                .balance(500000)
                .product(Product.우리닷컴통장)
                .isConnectedWibeeCard(true)
                .build();

        Account account4 = Account.builder()
                .user(user2)
                .accountNumber("4444444444444")
                .balance(600000)
                .product(Product.우리닷컴통장)
                .isConnectedWibeeCard(false)
                .build();

        Account account5 = Account.builder()
                .user(user3)
                .accountNumber("5555555555555")
                .balance(700000)
                .product(Product.우리아이행복통장)
                .isConnectedWibeeCard(false)
                .build();

        Account account6 = Account.builder()
                .user(user4)
                .accountNumber("6666666666666")
                .balance(800000)
                .product(Product.으쓱통장)
                .isConnectedWibeeCard(false)
                .build();

        Account account7 = Account.builder()
                .user(user4)
                .accountNumber("7777777777777")
                .balance(900000)
                .product(Product.WON통장)
                .isConnectedWibeeCard(false)
                .build();

        Account account8 = Account.builder()
                .user(user5)
                .accountNumber("8888888888888")
                .balance(850000)
                .product(Product.WON통장)
                .isConnectedWibeeCard(false)
                .build();

        accountRepository.save(account0);
        accountRepository.save(account1);
        accountRepository.save(account2);
        accountRepository.save(account3);
        accountRepository.save(account4);
        accountRepository.save(account5);
        accountRepository.save(account6);
        accountRepository.save(account7);
        accountRepository.save(account8);

        // 연결된 계좌 업데이트 (연결 계좌 설정)
        user1.updateConnectedAccount(account1);
        user1.updateWibeeCardAccount(account1);
        user2.updateConnectedAccount(account2);
        user2.updateWibeeCardAccount(account2);
        user3.updateConnectedAccount(account3);
        user3.updateWibeeCardAccount(account3);
        user4.updateConnectedAccount(account7);
        user5.updateConnectedAccount(account8);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
    }
}