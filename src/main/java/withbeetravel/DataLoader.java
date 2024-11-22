package withbeetravel;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import withbeetravel.domain.*;
import withbeetravel.repository.*;

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
    private final HistoryRepository historyRepository;

    @Override
    public void run(String... args) throws Exception {
        // User 더미 데이터 생성
        User user1 = User.builder()
                .email("user1@example.com")
                .password("password123")
                .pinNumber("123456")
                .name("신짱구")
                .profileImage(1) // Assuming 1 represents a profile image ID
                .failedPinCount(0)
                .build();
        User user2 = User.builder()
                .email("user2@example.com")
                .password("password123")
                .pinNumber("789012")
                .name("김철수")
                .profileImage(2)
                .failedPinCount(0)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        // 사용자마다 2개의 계좌 생성
        createAccountAndHistory(user1);
        createAccountAndHistory(user2);

        // Travel 더미 데이터 생성
        Travel travel1 = Travel.builder()
                .travelName("Summer Vacation 2024")
                .travelStartDate(LocalDate.of(2024, 6, 1))
                .travelEndDate(LocalDate.of(2024, 6, 7))
                .inviteCode("SUMMER2024")
                .mainImage("summer_vacation.jpg")
                .isDomesticTravel(true)
                .settlementStatus(SettlementStatus.PENDING)
                .build();
        Travel travel2 = Travel.builder()
                .travelName("Winter Trip 2024")
                .travelStartDate(LocalDate.of(2024, 12, 1))
                .travelEndDate(LocalDate.of(2024, 12, 5))
                .inviteCode("WINTER2024")
                .mainImage("winter_trip.jpg")
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
                .connectedAccount(account1)
                .build();
        TravelMember travelMember2 = TravelMember.builder()
                .travel(travel1)
                .user(user2)
                .isCaptain(false)
                .connectedAccount(account3)
                .build();
        TravelMember travelMember3 = TravelMember.builder()
                .travel(travel2)
                .user(user1)
                .isCaptain(false)
                .connectedAccount(account1)
                .build();
        TravelMember travelMember4 = TravelMember.builder()
                .travel(travel2)
                .user(user2)
                .isCaptain(true)
                .connectedAccount(account3)
                .build();
        TravelMember travelMember5 = TravelMember.builder()
                .travel(travel1)
                .user(user3)
                .isCaptain(false)
                .connectedAccount(account5)
                .build();
        travelMemberRepository.save(travelMember1);
        travelMemberRepository.save(travelMember2);
        travelMemberRepository.save(travelMember3);
        travelMemberRepository.save(travelMember4);
        travelMemberRepository.save(travelMember5);

        // SharedPayment와 PaymentParticipatedMembers 더미 데이터 30개 추가 생성
        for (int i = 0; i < 30; i++) {
            SharedPayment payment = SharedPayment.builder()
                    .addedByMember(i % 3 == 0 ? travelMember1 :
                            i % 3 == 1 ? travelMember2 : travelMember5)
                    .travel(travel1)
                    .currencyUnit(i % 3 == 0 ? CurrencyUnit.USD :
                            i % 3 == 1 ? CurrencyUnit.KRW : CurrencyUnit.JPY)
                    .paymentAmount(10000 + (i * 1000))
                    .foreignPaymentAmount(100.0 + i)
                    .exchangeRate(i % 3 == 0 ? 1.0 :
                            i % 3 == 1 ? 1000.0 : 100.0)
                    .paymentComment("Payment " + (i + 5))
                    .paymentImage(null)
                    .isManuallyAdded(i % 2 == 0)
                    .participantCount(i % 3 + 1)  // 1~3명이 참여
                    .category(i % 5 == 0 ? Category.FOOD :
                            i % 5 == 1 ? Category.TRANSPORTATION :
                                    i % 5 == 2 ? Category.ACCOMMODATION :
                                            i % 5 == 3 ? Category.SHOPPING :
                                                    Category.ACTIVITY)
                    .storeName("Store " + (i + 5))
                    .paymentDate(LocalDateTime.of(2024, 6,
                            2 + (i % 5),  // 6월 2일~6일
                            10 + (i % 14),  // 10시~23시
                            0 + (i % 60),  // 0분~59분
                            0))
                    .build();

            SharedPayment savedPayment = sharedPaymentRepository.save(payment);

            // 결제 참여자 추가 (participantCount에 따라 1~3명)
            if (i % 3 == 0) {  // 1명 참여
                PaymentParticipatedMember participant1 = PaymentParticipatedMember.builder()
                        .sharedPayment(savedPayment)
                        .travelMember(travelMember1)
                        .build();
                paymentParticipatedMemberRepository.save(participant1);
            } else if (i % 3 == 1) {  // 2명 참여
                PaymentParticipatedMember participant1 = PaymentParticipatedMember.builder()
                        .sharedPayment(savedPayment)
                        .travelMember(travelMember1)
                        .build();
                PaymentParticipatedMember participant2 = PaymentParticipatedMember.builder()
                        .sharedPayment(savedPayment)
                        .travelMember(travelMember2)
                        .build();
                paymentParticipatedMemberRepository.save(participant1);
                paymentParticipatedMemberRepository.save(participant2);
            } else {  // 3명 참여
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
                        .travelMember(travelMember5)
                        .build();
                paymentParticipatedMemberRepository.save(participant1);
                paymentParticipatedMemberRepository.save(participant2);
                paymentParticipatedMemberRepository.save(participant3);
            }
        }
    }
}
