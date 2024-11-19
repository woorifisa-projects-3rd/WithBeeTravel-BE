package withbeetravel;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import withbeetravel.domain.*;
import withbeetravel.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TravelRepository travelRepository;
    private final TravelMemberRepository travelMemberRepository;
    private final SharedPaymentRepository sharedPaymentRepository;
    private final AccountRepository accountRepository;
    private final HistoryRepository historyRepository;

    @Override
    public void run(String... args) throws Exception {
        // User dummy data
        User user1 = User.builder()
                .email("user1@example.com")
                .password("password123")
                .pinNumber("123456")
                .name("User One")
                .profileImage("profile1.jpg")
                .build();
        User user2 = User.builder()
                .email("user2@example.com")
                .password("password123")
                .pinNumber("567890")
                .name("User Two")
                .profileImage("profile2.jpg")
                .build();

        userRepository.saveAll(Arrays.asList(user1, user2));

        // Account dummy data
        Account account1 = Account.builder()
                .user(user1)
                .accountNumber("123-456-789")
                .balance(5000)
                .product(Product.WON통장)
                .isConnectedWibeeCard(true)
                .build();

        Account account2 = Account.builder()
                .user(user1)
                .accountNumber("987-654-321")
                .balance(20000)
                .product(Product.우리닷컴통장)
                .isConnectedWibeeCard(false)
                .build();

        Account account3 = Account.builder()
                .user(user1)
                .accountNumber("111-222-333")
                .balance(15000)
                .product(Product.우리아이행복통장)
                .isConnectedWibeeCard(false)
                .build();

        Account account4 = Account.builder()
                .user(user2)
                .accountNumber("444-555-666")
                .balance(8000)
                .product(Product.WON파킹통장)
                .isConnectedWibeeCard(false)
                .build();

        Account account5 = Account.builder()
                .user(user2)
                .accountNumber("777-888-999")
                .balance(30000)
                .product(Product.으쓱통장)
                .isConnectedWibeeCard(false)
                .build();

        // Save accounts to the repository
        accountRepository.saveAll(Arrays.asList(account1, account2, account3, account4, account5));

        // Save updated users with linked accounts
        userRepository.saveAll(Arrays.asList(user1));

        History history1 = History.builder()
                .account(account1)
                .date(LocalDateTime.of(2024, 11, 1, 0, 0))
                .rcvAm(1000)
                .payAM(0)
                .balance(6000)
                .rqspeNm("Deposit")
                .isWibeeCard(false)
                .build();

        History history2 = History.builder()
                .account(account1)
                .date(LocalDateTime.of(2024, 11, 1, 0, 0))
                .rcvAm(0)
                .payAM(1000)
                .balance(5000)
                .rqspeNm("Deposit")
                .isWibeeCard(true)
                .build();

        History history3 = History.builder()
                .account(account1)
                .date(LocalDateTime.of(2024, 11, 2, 0, 0))
                .rcvAm(0)
                .payAM(1000)
                .balance(5000)
                .rqspeNm("Deposit")
                .isWibeeCard(true)
                .build();

        History history4 = History.builder()
                .account(account1)
                .date(LocalDateTime.of(2024, 11, 4, 0, 0))
                .rcvAm(0)
                .payAM(1000)
                .balance(5000)
                .rqspeNm("Deposit")
                .isWibeeCard(true)
                .build();

        History history5 = History.builder()
                .account(account2)
                .date(LocalDateTime.of(2024, 11, 4, 0, 0))
                .rcvAm(0)
                .payAM(1000)
                .balance(5000)
                .rqspeNm("Deposit")
                .isWibeeCard(true)
                .build();

        // Save histories to the repository
        historyRepository.saveAll(Arrays.asList(history1, history2, history3, history4, history5));

        // Travel dummy data
        Travel travel1 = Travel.builder()
                .travelName("Vacation to Bali")
                .travelStartDate(LocalDate.of(2024, 11, 2))
                .travelEndDate(LocalDate.of(2024, 11, 3))
                .inviteCode("BALI1234")
                .mainImage("bali_trip.jpg")
                .isDomesticTravel(false)
                .settlementStatus(SettlementStatus.PENDING)
                .build();

        Travel travel2 = Travel.builder()
                .travelName("Summer Trip to Jeju")
                .travelStartDate(LocalDate.of(2024, 6, 15))
                .travelEndDate(LocalDate.of(2024, 6, 22))
                .inviteCode("JEJU2024")
                .mainImage("jeju_trip.jpg")
                .isDomesticTravel(true)
                .settlementStatus(SettlementStatus.PENDING)
                .build();

        travelRepository.saveAll(Arrays.asList(travel1, travel2));

        // TravelMember dummy data
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
                .connectedAccount(account2)
                .build();

        TravelMember travelMember3 = TravelMember.builder()
                .travel(travel2)
                .user(user1)
                .isCaptain(true)
                .connectedAccount(account3)
                .build();

        TravelMember travelMember4 = TravelMember.builder()
                .travel(travel2)
                .user(user2)
                .isCaptain(false)
                .connectedAccount(account4)
                .build();

        // Save TravelMember data
        travelMemberRepository.saveAll(Arrays.asList(travelMember1, travelMember2, travelMember3, travelMember4));

        SharedPayment sharedPayment1 = SharedPayment.builder()
                .addedByMember(travelMember1)
                .travel(travel1)
                .currencyUnit(CurrencyUnit.KRW)
                .paymentAmount(150000)
                .foreignPaymentAmount(120.0)
                .exchangeRate(1250.5)
                .paymentComment("Lunch at restaurant")
                .paymentImage("lunch_image.jpg")
                .isManuallyAdded(false)
                .participantCount(4)
                .category(Category.FOOD)
                .storeName("Kimchi Restaurant")
                .paymentDate(LocalDateTime.of(2024, 11, 18, 12, 30))
                .build();

        SharedPayment sharedPayment2 = SharedPayment.builder()
                .addedByMember(travelMember1)
                .travel(travel1)
                .currencyUnit(CurrencyUnit.USD)
                .paymentAmount(200)
                .foreignPaymentAmount(200.0)
                .exchangeRate(1.0)
                .paymentComment("Train ticket to museum")
                .paymentImage("train_ticket.jpg")
                .isManuallyAdded(true)
                .participantCount(5)
                .category(Category.TRANSPORTATION)
                .storeName("Museum Train Ticket Booth")
                .paymentDate(LocalDateTime.of(2024, 11, 18, 14, 15))
                .build();

        SharedPayment sharedPayment3 = SharedPayment.builder()
                .addedByMember(travelMember1)
                .travel(travel1)
                .currencyUnit(CurrencyUnit.EUR)
                .paymentAmount(75)
                .foreignPaymentAmount(85.0)
                .exchangeRate(1.14)
                .paymentComment("Hotel stay for 2 nights")
                .paymentImage("hotel_stay.jpg")
                .isManuallyAdded(true)
                .participantCount(2)
                .category(Category.ACCOMMODATION)
                .storeName("City Hotel")
                .paymentDate(LocalDateTime.of(2024, 11, 17, 18, 0))
                .build();

        SharedPayment sharedPayment4 = SharedPayment.builder()
                .addedByMember(travelMember1)
                .travel(travel1)
                .currencyUnit(CurrencyUnit.CNY)
                .paymentAmount(500)
                .foreignPaymentAmount(72.0)
                .exchangeRate(6.94)
                .paymentComment("Shopping in the mall")
                .paymentImage("shopping_receipt.jpg")
                .isManuallyAdded(false)
                .participantCount(3)
                .category(Category.SHOPPING)
                .storeName("Beijing Mall")
                .paymentDate(LocalDateTime.of(2024, 11, 19, 10, 0))
                .build();

        SharedPayment sharedPayment5 = SharedPayment.builder()
                .addedByMember(travelMember1)
                .travel(travel1)
                .currencyUnit(CurrencyUnit.JPY)
                .paymentAmount(3000)
                .foreignPaymentAmount(3000.0)
                .exchangeRate(0.008)
                .paymentComment("Flight booking")
                .paymentImage("flight_booking.jpg")
                .isManuallyAdded(false)
                .participantCount(6)
                .category(Category.FLIGHT)
                .storeName("Airline Booking")
                .paymentDate(LocalDateTime.of(2024, 11, 20, 11, 45))
                .build();

        sharedPaymentRepository.saveAll(Arrays.asList(sharedPayment1, sharedPayment2, sharedPayment3, sharedPayment4, sharedPayment5));
    }
}