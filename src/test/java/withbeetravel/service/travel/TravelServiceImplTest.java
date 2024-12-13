package withbeetravel.service.travel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import withbeetravel.domain.*;
import withbeetravel.dto.request.travel.TravelRequest;
import withbeetravel.dto.response.travel.TravelResponse;
import withbeetravel.repository.*;
import withbeetravel.exception.CustomException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TravelServiceImplTest {

    @Mock
    private TravelRepository travelRepository;

    @Mock
    private TravelCountryRepository travelCountryRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TravelMemberRepository travelMemberRepository;

    @InjectMocks
    private TravelServiceImpl travelService;

    private User mockUser;
    private Account mockAccount;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .password("password123!") // 암호를 반드시 설정
                .name("Test User")
                .email("test@example.com")
                .pinNumber("1234") // 핀 번호 추가
                .build();

        mockAccount = Account.builder()
                .isConnectedWibeeCard(true)
                .user(mockUser)
                .build();
    }

    @Test
    void saveTravel_국내여행_성공() {
        // Given
        TravelRequest request = new TravelRequest(
                "서울 여행",
                true,
                null,
                "2024-07-01",
                "2024-07-05"
        );

        // Mock 설정
        when(accountRepository.findByUserId(anyLong()))
                .thenReturn(List.of(mockAccount));

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(mockUser));

        Travel savedTravel = Travel.builder()
                .id(1L)
                .travelName(request.getTravelName())
                .travelStartDate(LocalDate.parse(request.getTravelStartDate()))
                .travelEndDate(LocalDate.parse(request.getTravelEndDate()))
                .isDomesticTravel(request.isDomesticTravel())
                .build();

        when(travelRepository.save(any(Travel.class)))
                .thenReturn(savedTravel);

        // When
        TravelResponse response = travelService.saveTravel(request, 1L);

        // Then
        assertNotNull(response);
        assertEquals("서울 여행", response.getName());
        assertEquals(LocalDate.parse("2024-07-01").toString(), response.getStartDate());
        assertEquals(LocalDate.parse("2024-07-05").toString(), response.getEndDate());

        // Verify method calls
        verify(accountRepository).findByUserId(1L);
        verify(travelRepository).save(any(Travel.class));
        verify(travelMemberRepository).save(any(TravelMember.class));
    }

    @Test
    void saveTravel_해외여행_성공() {
        // Given
        TravelRequest request = new TravelRequest(
                "유럽 여행",
                false,
                List.of("프랑스", "독일"), // ISO 코드와 이름 혼합
                "2024-08-01",
                "2024-08-15"
        );

        // Mock 설정
        when(accountRepository.findByUserId(anyLong()))
                .thenReturn(List.of(mockAccount));

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(mockUser));

        Travel savedTravel = Travel.builder()
                .id(1L)
                .travelName(request.getTravelName())
                .travelStartDate(LocalDate.parse(request.getTravelStartDate()))
                .travelEndDate(LocalDate.parse(request.getTravelEndDate()))
                .isDomesticTravel(request.isDomesticTravel())
                .build();

        when(travelRepository.save(any(Travel.class)))
                .thenReturn(savedTravel);

        // 국가 모킹
        List<TravelCountry> travelCountries = request.getTravelCountries().stream()
                .map(countryName -> TravelCountry.builder()
                        .country(Country.findByName(countryName))
                        .travel(savedTravel)
                        .build())
                .toList();

        // When
        TravelResponse response = travelService.saveTravel(request, 1L);

        // Then
        assertNotNull(response);
        assertEquals("유럽 여행", response.getName());
        assertEquals(LocalDate.parse("2024-08-01").toString(), response.getStartDate());
        assertEquals(LocalDate.parse("2024-08-15").toString(), response.getEndDate());
        assertEquals(List.of("FR", "DE"), response.getCountry());

        // Verify method calls
        verify(accountRepository).findByUserId(1L);
        verify(travelRepository).save(any(Travel.class));
        verify(travelMemberRepository).save(any(TravelMember.class));
        verify(travelCountryRepository).saveAll(anyList());
    }

    @Test
    void saveTravel_위비카드미연결_예외발생() {
        // Given
        TravelRequest request = new TravelRequest(
                "제주도 여행",
                true,
                null,
                "2024-09-01",
                "2024-09-05"
        );

        // Mock 설정 - 위비 카드 미연결 상태
        Account unconnectedAccount = Account.builder()
                .isConnectedWibeeCard(false)
                .user(mockUser)
                .build();

        when(accountRepository.findByUserId(anyLong()))
                .thenReturn(List.of(unconnectedAccount));

        // When & Then
        assertThrows(CustomException.class,
                () -> travelService.saveTravel(request, 1L),
                "위비 카드 미연결 시 예외가 발생해야 합니다.");

        verify(accountRepository).findByUserId(1L);
        verify(travelRepository, never()).save(any(Travel.class));
    }

    @Test
    void editTravel_국내여행_성공() {
        // Given
        Long travelId = 1L;
        TravelRequest request = new TravelRequest(
                "수정된 여행",
                true,
                null,
                "2024-10-01",
                "2024-10-10"
        );

        Travel existingTravel = Travel.builder()
                .id(travelId)
                .travelName("기존 여행")
                .travelStartDate(LocalDate.parse("2024-09-01"))
                .travelEndDate(LocalDate.parse("2024-09-10"))
                .isDomesticTravel(true)
                .build();

        // Mock 설정
        when(travelRepository.findById(travelId))
                .thenReturn(Optional.of(existingTravel));

        // When
        travelService.editTravel(request, travelId);

        // Then
        verify(travelRepository).findById(travelId);
        verify(travelCountryRepository).deleteByTravel(existingTravel);

        // 여행 정보가 정확히 수정되었는지 확인
        assertEquals("수정된 여행", existingTravel.getTravelName());
        assertEquals(LocalDate.parse("2024-10-01"), existingTravel.getTravelStartDate());
        assertEquals(LocalDate.parse("2024-10-10"), existingTravel.getTravelEndDate());
    }

    @Test
    void editTravel_해외여행_성공() {
        // Given
        Long travelId = 1L;
        TravelRequest request = new TravelRequest(
                "수정된 해외 여행",
                false,
                List.of("이탈리아", "스페인"),
                "2024-11-01",
                "2024-11-15"
        );

        Travel existingTravel = Travel.builder()
                .id(travelId)
                .travelName("기존 해외 여행")
                .travelStartDate(LocalDate.parse("2024-09-01"))
                .travelEndDate(LocalDate.parse("2024-09-10"))
                .isDomesticTravel(true)
                .build();

        // Mock 설정
        when(travelRepository.findById(travelId))
                .thenReturn(Optional.of(existingTravel));

        // When
        travelService.editTravel(request, travelId);

        // Then
        verify(travelRepository).findById(travelId);
        verify(travelCountryRepository).deleteByTravel(existingTravel);
        verify(travelCountryRepository).saveAll(anyList());

        // 여행 정보가 정확히 수정되었는지 확인
        assertEquals("수정된 해외 여행", existingTravel.getTravelName());
        assertEquals(LocalDate.parse("2024-11-01"), existingTravel.getTravelStartDate());
        assertEquals(LocalDate.parse("2024-11-15"), existingTravel.getTravelEndDate());
        assertFalse(existingTravel.isDomesticTravel());
    }

    @Test
    void editTravel_여행_미존재_예외발생() {
        // Given
        Long travelId = 999L;
        TravelRequest request = new TravelRequest(
                "존재하지 않는 여행",
                true,
                null,
                "2024-12-01",
                "2024-12-10"
        );

        // Mock 설정
        when(travelRepository.findById(travelId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> travelService.editTravel(request, travelId),
                "존재하지 않는 여행 ID로 수정 시 예외가 발생해야 합니다.");

        verify(travelRepository).findById(travelId);
        verify(travelCountryRepository, never()).deleteByTravel(any());
    }
}