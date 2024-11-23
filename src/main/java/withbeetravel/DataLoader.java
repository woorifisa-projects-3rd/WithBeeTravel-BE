package withbeetravel;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import withbeetravel.domain.*;
import withbeetravel.repository.*;
import withbeetravel.service.payment.SharedPaymentCategoryClassificationService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TravelRepository travelRepository;
    private final TravelMemberRepository travelMemberRepository;
    private final SharedPaymentRepository sharedPaymentRepository;
    private final PaymentParticipatedMemberRepository paymentParticipatedMemberRepository;
    private final TravelCountryRepository travelCountryRepository;
    private final SharedPaymentCategoryClassificationService sharedPaymentCategoryClassificationService;


    @Override
    public void run(String... args) throws Exception {
        // User 더미 데이터 생성
        User user1 = User.builder()
                .email("lee@example.com")
                .password("password123")
                .pinNumber("123456")
                .name("이도이")
                .profileImage(1)
                .build();
        User user2 = User.builder()
                .email("gong@example.com")
                .password("password123")
                .pinNumber("234567")
                .name("공소연")
                .profileImage(2)
                .build();
        User user3 = User.builder()
                .email("yoo@example.com")
                .password("password123")
                .pinNumber("345678")
                .name("유승아")
                .profileImage(3)
                .build();
        User user4 = User.builder()
                .email("kim@example.com")
                .password("password123")
                .pinNumber("456789")
                .name("김호철")
                .profileImage(4)
                .build();
        User user5 = User.builder()
                .email("kong@example.com")
                .password("password123")
                .pinNumber("567890")
                .name("공예진")
                .profileImage(5)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);  // 추가
        userRepository.save(user5);  // 추가

        // Account 더미 데이터 생성
        Account account1 = Account.builder()
                .user(user1)
                .accountNumber("1234-5678-9012")
                .balance(1000000)
                .product(Product.WON통장)
                .isConnectedWibeeCard(true)
                .build();

        Account account2 = Account.builder()
                .user(user1)
                .accountNumber("2345-6789-0123")
                .balance(2000000)
                .product(Product.WON파킹통장)
                .isConnectedWibeeCard(false)
                .build();

        Account account3 = Account.builder()
                .user(user2)
                .accountNumber("3456-7890-1234")
                .balance(1500000)
                .product(Product.보통예금)
                .isConnectedWibeeCard(true)
                .build();

        Account account4 = Account.builder()
                .user(user2)
                .accountNumber("4567-8901-2345")
                .balance(3000000)
                .product(Product.으쓱통장)
                .isConnectedWibeeCard(false)
                .build();

        Account account5 = Account.builder()
                .user(user3)
                .accountNumber("5678-9012-3456")
                .balance(2500000)
                .product(Product.우리아이행복통장)
                .isConnectedWibeeCard(false)
                .build();

        accountRepository.save(account1);
        accountRepository.save(account2);
        accountRepository.save(account3);
        accountRepository.save(account4);
        accountRepository.save(account5);

        // Travel 더미 데이터 생성
        Travel travel1 = Travel.builder()
                .travelName("제주도 여름 여행")
                .travelStartDate(LocalDate.of(2024, 6, 1))
                .travelEndDate(LocalDate.of(2024, 6, 7))
                .inviteCode("SUMMER2024")
                .mainImage(null)
                .isDomesticTravel(true)
                .settlementStatus(SettlementStatus.PENDING)
                .build();

        Travel travel2 = Travel.builder()
                .travelName("일본 겨울 여행")
                .travelStartDate(LocalDate.of(2024, 12, 1))
                .travelEndDate(LocalDate.of(2024, 12, 5))
                .inviteCode("WINTER2024")
                .mainImage(null)
                .isDomesticTravel(false)
                .settlementStatus(SettlementStatus.DONE)
                .build();

        travelRepository.save(travel1);
        travelRepository.save(travel2);

        // TravelMember 더미 데이터 생성
        TravelMember travelMember1 = TravelMember.builder()
                .travel(travel1)
                .user(user1)
                .isCaptain(true)
                .build();
        TravelMember travelMember2 = TravelMember.builder()
                .travel(travel1)
                .user(user2)
                .isCaptain(false)
                .build();
        TravelMember travelMember3 = TravelMember.builder()
                .travel(travel1)
                .user(user3)
                .isCaptain(false)
                .build();
        TravelMember travelMember4 = TravelMember.builder()
                .travel(travel1)
                .user(user4)
                .isCaptain(false)
                .build();
        TravelMember travelMember5 = TravelMember.builder()
                .travel(travel1)
                .user(user5)
                .isCaptain(false)
                .build();

        // travel2의 멤버
        TravelMember travelMember6 = TravelMember.builder()
                .travel(travel2)
                .user(user1)
                .isCaptain(false)
                .build();
        TravelMember travelMember7 = TravelMember.builder()
                .travel(travel2)
                .user(user2)
                .isCaptain(true)
                .build();

        travelMemberRepository.save(travelMember1);
        travelMemberRepository.save(travelMember2);
        travelMemberRepository.save(travelMember3);
        travelMemberRepository.save(travelMember4);
        travelMemberRepository.save(travelMember5);
        travelMemberRepository.save(travelMember6);
        travelMemberRepository.save(travelMember7);

        // SharedPayment와 PaymentParticipatedMembers 더미 데이터 30개 추가 생성
        for (int i = 0; i < 30; i++) {
            SharedPayment payment = SharedPayment.builder()
                    .addedByMember(i % 5 == 0 ? travelMember1 :  // 5명이 돌아가면서 결제
                            i % 5 == 1 ? travelMember2 :
                                    i % 5 == 2 ? travelMember3 :
                                            i % 5 == 3 ? travelMember4 :
                                                    travelMember5)
                    .travel(travel1)
                    .currencyUnit(i % 3 == 0 ? CurrencyUnit.USD :
                            i % 3 == 1 ? CurrencyUnit.KRW : CurrencyUnit.JPY)
                    .paymentAmount(10000 + (i * 1000))
                    .foreignPaymentAmount(100.0 + i)
                    .exchangeRate(i % 3 == 0 ? 1.0 :
                            i % 3 == 1 ? 1000.0 : 100.0)
                    .paymentComment(i % 5 == 0 ? "흑돼지 삼겹살 2인분" :
                            i % 5 == 1 ? "아메리카노 3잔" :
                                    i % 5 == 2 ? "숙박비 1박" :
                                            i % 5 == 3 ? "기념품 구매" :
                                                    "호텔 수영장 이용")
                    .paymentImage(null)
                    .isManuallyAdded(i % 2 == 0)
                    .participantCount(5)  // 모든 멤버가 참여하므로 5로 고정
                    .storeName(i % 5 == 0 ? "제주 흑돼지 맛집" :
                            i % 5 == 1 ? "서귀포 카페" :
                                    i % 5 == 2 ? "제주 게스트하우스" :
                                            i % 5 == 3 ? "올레마켓" :
                                                    "제주관광호텔")
                    .category(sharedPaymentCategoryClassificationService.getCategory(
                            i % 5 == 0 ? "제주 흑돼지 맛집" :
                                    i % 5 == 1 ? "서귀포 카페" :
                                            i % 5 == 2 ? "제주 게스트하우스" :
                                                    i % 5 == 3 ? "올레마켓" :
                                                            "제주관광호텔"
                    ))
                    .paymentDate(LocalDateTime.of(2024, 6,
                            2 + (i % 5),  // 6월 2일~6일
                            10 + (i % 14),  // 10시~23시
                            0 + (i % 60),  // 0분~59분
                            0))
                    .build();

            SharedPayment savedPayment = sharedPaymentRepository.save(payment);

            // 모든 멤버를 참여자로 추가
            PaymentParticipatedMember participant1 = PaymentParticipatedMember.builder()
                    .sharedPayment(savedPayment)
                    .travelMember(travelMember1)
                    .build();
            PaymentParticipatedMember participant2 = PaymentParticipatedMember.builder()
                    .sharedPayment(savedPayment)
                    .travelMember(travelMember2)
                    .build();
            PaymentParticipatedMember participant3 = PaymentParticipatedMember.builder()
                    .sharedPayment(savedPayment)
                    .travelMember(travelMember3)
                    .build();
            PaymentParticipatedMember participant4 = PaymentParticipatedMember.builder()
                    .sharedPayment(savedPayment)
                    .travelMember(travelMember4)
                    .build();
            PaymentParticipatedMember participant5 = PaymentParticipatedMember.builder()
                    .sharedPayment(savedPayment)
                    .travelMember(travelMember5)
                    .build();

            paymentParticipatedMemberRepository.save(participant1);
            paymentParticipatedMemberRepository.save(participant2);
            paymentParticipatedMemberRepository.save(participant3);
            paymentParticipatedMemberRepository.save(participant4);
            paymentParticipatedMemberRepository.save(participant5);
        }

        TravelCountry travelCountry1 = TravelCountry.builder()
                .travel(travel1)
                .country(Country.JP)
                .build();
        TravelCountry travelCountry2 = TravelCountry.builder()
                .travel(travel1)
                .country(Country.CN)
                .build();
        TravelCountry travelCountry3 = TravelCountry.builder()
                .travel(travel2)
                .country(Country.KR)
                .build();

        // TravelCountry 저장
        travelCountryRepository.save(travelCountry1);
        travelCountryRepository.save(travelCountry2);
        travelCountryRepository.save(travelCountry3);
    }
}
