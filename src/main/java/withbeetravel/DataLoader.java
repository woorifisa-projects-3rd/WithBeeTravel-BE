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
    private final TravelRepository travelRepository;
    private final TravelMemberRepository travelMemberRepository;
    private final SharedPaymentRepository sharedPaymentRepository;
    private final PaymentParticipatedMemberRepository paymentParticipatedMemberRepository;

    @Override
    public void run(String... args) throws Exception {
        // User 더미 데이터 생성
        User user1 = User.builder()
                .email("user1@example.com")
                .password("password123")
                .pinNumber("123456")
                .name("User One")
                .profileImage("profile1.jpg")
                .hasWibeeCard(true)
                .build();
        User user2 = User.builder()
                .email("user2@example.com")
                .password("password123")
                .pinNumber("567890")
                .name("User Two")
                .profileImage("profile2.jpg")
                .hasWibeeCard(false)
                .build();
        User user3 = User.builder()
                .email("user3@example.com")
                .password("password123")
                .pinNumber("567890")
                .name("User Three")
                .profileImage("profile3.jpg")
                .hasWibeeCard(false)
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

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
                .settlementStatus(SettlementStatus.PENDING)
                .build();
        travelRepository.save(travel1);
        travelRepository.save(travel2);

        // TravelMember 더미 데이터 생성
        TravelMember travelMember1 = TravelMember.builder()
                .travel(travel1)
                .user(user1)
                .isCaptain(true)
                .connectedAccount("user1_account")
                .build();
        TravelMember travelMember2 = TravelMember.builder()
                .travel(travel1)
                .user(user2)
                .isCaptain(false)
                .connectedAccount("user2_account")
                .build();
        TravelMember travelMember3 = TravelMember.builder()
                .travel(travel2)
                .user(user1)
                .isCaptain(false)
                .connectedAccount("user1_account")
                .build();
        TravelMember travelMember4 = TravelMember.builder()
                .travel(travel2)
                .user(user2)
                .isCaptain(true)
                .connectedAccount("user2_account")
                .build();
        travelMemberRepository.save(travelMember1);
        travelMemberRepository.save(travelMember2);
        travelMemberRepository.save(travelMember3);
        travelMemberRepository.save(travelMember4);

        // SharedPayment 더미 데이터 생성
        for (int i = 0; i < 30; i++) {
            SharedPayment payment = SharedPayment.builder()
                    .addedByMember(i % 2 == 0 ? travelMember1 : travelMember2) // travel1의 멤버들이 번갈아가며 추가
                    .travel(travel1)
                    .currencyUnit(i % 3 == 0 ? CurrencyUnit.USD :
                            i % 3 == 1 ? CurrencyUnit.KRW : CurrencyUnit.JPY)
                    .paymentAmount(10000 + (i * 1000))
                    .foreignPaymentAmount(100.0 + i)
                    .exchangeRate(i % 3 == 0 ? 1.0 :
                            i % 3 == 1 ? 1000.0 : 100.0)
                    .paymentComment("Payment " + (i + 5))
                    .paymentImage(i % 2 == 0 ? "https://withbee-travel.s3.ap-northeast-2.amazonaws.com/shared-payments/image_" + (i + 5) + ".png" : null)
                    .isManuallyAdded(i % 2 == 0)
                    .participantCount(2)
                    .category(i % 5 == 0 ? Category.FOOD :
                            i % 5 == 1 ? Category.TRANSPORTATION :
                                    i % 5 == 2 ? Category.ACCOMMODATION :
                                            i % 5 == 3 ? Category.SHOPPING :
                                                    Category.ACTIVITY)
                    .storeName("Store " + (i + 5))
                    .paymentDate(LocalDateTime.of(2024, 6, 2 + (i % 5), 10 + (i % 14), 0))
                    .build();

            SharedPayment savedPayment = sharedPaymentRepository.save(payment);

            // 각 payment의 참여자 추가
            PaymentParticipatedMember participatedMember1 = PaymentParticipatedMember.builder()
                    .sharedPayment(savedPayment)
                    .travelMember(travelMember1)
                    .build();
            paymentParticipatedMemberRepository.save(participatedMember1);

            // 짝수 번호의 payment는 두 명이 참여
            if (i % 2 == 0) {
                PaymentParticipatedMember participatedMember2 = PaymentParticipatedMember.builder()
                        .sharedPayment(savedPayment)
                        .travelMember(travelMember2)
                        .build();
                paymentParticipatedMemberRepository.save(participatedMember2);
            }
        }
    }
}
