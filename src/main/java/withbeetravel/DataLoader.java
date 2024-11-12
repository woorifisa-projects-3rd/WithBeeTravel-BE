package withbeetravel;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import withbeetravel.domain.SettlementStatus;
import withbeetravel.domain.Travel;
import withbeetravel.domain.TravelMember;
import withbeetravel.domain.User;
import withbeetravel.repository.TravelMemberRepository;
import withbeetravel.repository.TravelRepository;
import withbeetravel.repository.UserRepository;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TravelRepository travelRepository;
    private final TravelMemberRepository travelMemberRepository;

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
        userRepository.save(user1);
        userRepository.save(user2);

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
    }
}
