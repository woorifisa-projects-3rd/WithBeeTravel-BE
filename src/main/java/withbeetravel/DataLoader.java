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
                .settlementStatus(SettlementStatus.DONE)
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
        TravelMember travelMember5 = TravelMember.builder()
                .travel(travel1)
                .user(user3)
                .isCaptain(false)
                .connectedAccount("user3_account")
                .build();
        travelMemberRepository.save(travelMember1);
        travelMemberRepository.save(travelMember2);
        travelMemberRepository.save(travelMember3);
        travelMemberRepository.save(travelMember4);
        travelMemberRepository.save(travelMember5);

        // SharedPayment 더미 데이터 생성
        SharedPayment payment1 = SharedPayment.builder()
                .addedByMember(travelMember1)
                .travel(travel1)
                .currencyUnit(CurrencyUnit.USD)
                .paymentAmount(100)
                .foreignPaymentAmount(100.0)
                .exchangeRate(1.0)
                .paymentComment("Lunch")
                .paymentImage("https://withbee-travel.s3.ap-northeast-2.amazonaws.com/shared-payments/1/32b52c1e-7a53-4e25-80cd-4a8b5eedb0e4_%EA%BE%B8%EB%B2%85.png")
                .isManuallyAdded(true)
                .participantCount(3)
                .category(Category.FOOD)
                .storeName("Seafood Restaurant")
                .paymentDate(LocalDateTime.of(2024, 6, 2, 12, 0))
                .build();

        SharedPayment payment2 = SharedPayment.builder()
                .addedByMember(travelMember2)
                .travel(travel1)
                .currencyUnit(CurrencyUnit.KRW)
                .paymentAmount(50000)
                .foreignPaymentAmount(50.0)
                .exchangeRate(1000.0)
                .paymentComment("Taxi fare")
                .paymentImage(null)
                .isManuallyAdded(false)
                .participantCount(3)
                .category(Category.TRANSPORTATION)
                .storeName("Seoul Taxi Co.")
                .paymentDate(LocalDateTime.of(2024, 6, 2, 14, 30))
                .build();

        SharedPayment payment3 = SharedPayment.builder()
                .addedByMember(travelMember3)
                .travel(travel2)
                .currencyUnit(CurrencyUnit.JPY)
                .paymentAmount(2000)
                .foreignPaymentAmount(20.0)
                .exchangeRate(100.0)
                .paymentComment("Dinner")
                .paymentImage(null)
                .isManuallyAdded(true)
                .participantCount(2)
                .category(Category.FOOD)
                .storeName("Ramen Shop")
                .paymentDate(LocalDateTime.of(2024, 12, 2, 19, 0))
                .build();

        SharedPayment payment4 = SharedPayment.builder()
                .addedByMember(travelMember4)
                .travel(travel2)
                .currencyUnit(CurrencyUnit.USD)
                .paymentAmount(150)
                .foreignPaymentAmount(150.0)
                .exchangeRate(1.0)
                .paymentComment("Hotel booking")
                .paymentImage(null)
                .isManuallyAdded(false)
                .participantCount(2)
                .category(Category.ACCOMMODATION)
                .storeName("Tokyo Hotel")
                .paymentDate(LocalDateTime.of(2024, 12, 1, 15, 0))
                .build();

        sharedPaymentRepository.save(payment1);
        sharedPaymentRepository.save(payment2);
        sharedPaymentRepository.save(payment3);
        sharedPaymentRepository.save(payment4);

        // PaymentParticipatedMembers 더미 데이터 생성
        // payment1의 참여자들 (travelMember1, travelMember2, travelMember5)
        PaymentParticipatedMember paymentParticipatedMember1 = PaymentParticipatedMember.builder()
                .sharedPayment(payment1)
                .travelMember(travelMember1)
                .build();
        PaymentParticipatedMember paymentParticipatedMember2 = PaymentParticipatedMember.builder()
                .sharedPayment(payment1)
                .travelMember(travelMember2)
                .build();
        PaymentParticipatedMember paymentParticipatedMember3 = PaymentParticipatedMember.builder()
                .sharedPayment(payment1)
                .travelMember(travelMember5)
                .build();

        // payment2의 참여자들 (travelMember1, travelMember5)
        PaymentParticipatedMember paymentParticipatedMember4 = PaymentParticipatedMember.builder()
                .sharedPayment(payment2)
                .travelMember(travelMember1)
                .build();
        PaymentParticipatedMember paymentParticipatedMember6 = PaymentParticipatedMember.builder()
                .sharedPayment(payment2)
                .travelMember(travelMember5)
                .build();

        // payment3의 참여자들 (travelMember3, travelMember4)
        PaymentParticipatedMember paymentParticipatedMember7 = PaymentParticipatedMember.builder()
                .sharedPayment(payment3)
                .travelMember(travelMember3)
                .build();
        PaymentParticipatedMember paymentParticipatedMember8 = PaymentParticipatedMember.builder()
                .sharedPayment(payment3)
                .travelMember(travelMember4)
                .build();

        // payment4의 참여자들 (travelMember4)
        PaymentParticipatedMember paymentParticipatedMember10 = PaymentParticipatedMember.builder()
                .sharedPayment(payment4)
                .travelMember(travelMember4)
                .build();

        // PaymentParticipatedMember 저장
        paymentParticipatedMemberRepository.save(paymentParticipatedMember1);
        paymentParticipatedMemberRepository.save(paymentParticipatedMember2);
        paymentParticipatedMemberRepository.save(paymentParticipatedMember3);
        paymentParticipatedMemberRepository.save(paymentParticipatedMember4);
        paymentParticipatedMemberRepository.save(paymentParticipatedMember6);
        paymentParticipatedMemberRepository.save(paymentParticipatedMember7);
        paymentParticipatedMemberRepository.save(paymentParticipatedMember8);
        paymentParticipatedMemberRepository.save(paymentParticipatedMember10);
    }
}
