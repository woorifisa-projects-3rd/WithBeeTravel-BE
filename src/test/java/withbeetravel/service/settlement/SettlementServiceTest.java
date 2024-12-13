package withbeetravel.service.settlement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;
import withbeetravel.domain.*;
import withbeetravel.repository.*;
import withbeetravel.repository.notification.EmitterRepository;
import withbeetravel.support.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    @Mock private TravelMemberRepository travelMemberRepository;
    @Mock private SettlementRequestRepository settlementRequestRepository;
    @Mock private TravelMemberSettlementHistoryRepository travelMemberSettlementHistoryRepository;
    @Mock private PaymentParticipatedMemberRepository paymentParticipatedMemberRepository;
    @Mock private UserRepository userRepository;
    @Mock private TravelRepository travelRepository;
    @Mock private SharedPaymentRepository sharedPaymentRepository;
    @Mock private SettlementRequestLogRepository settlementRequestLogRepository;
    @Mock private EmitterRepository emitterRepository;
    @InjectMocks private SettlementServiceImpl settlementService;
    @Mock private TaskScheduler taskScheduler;


    @Test
    void 정산_요청을_할_수_있다() {

        TestData testData = SettlementTestFixture.createTestData();

        given(travelRepository.findById(testData.travel.getId())).willReturn(Optional.of(testData.travel));
        given(travelMemberRepository.findByTravelIdAndUserId(testData.travel.getId(), testData.user1.getId()))
                .willReturn(Optional.of(testData.travelMember1));
        given(travelMemberRepository.findAllByTravelId(testData.travel.getId()))
                .willReturn(List.of(testData.travelMember1, testData.travelMember2));

        given(settlementRequestRepository.save(any(SettlementRequest.class))).willReturn(testData.settlementRequest);

        given(travelMemberSettlementHistoryRepository.save(any(TravelMemberSettlementHistory.class))).willReturn(testData.settlementHistory2);

        given(paymentParticipatedMemberRepository.findAllByTravelMemberId(testData.travelMember1.getId())).willReturn(List.of(testData.pppm1, testData.pppm2));
        given(paymentParticipatedMemberRepository.findAllByTravelMemberId(testData.travelMember2.getId())).willReturn(List.of(testData.pppm3, testData.pppm4));

        given(settlementRequestLogRepository.save(any(SettlementRequestLog.class))).willReturn(SettlementRequestLog.builder().user(testData.user1).travel(testData.travel).build());

        // 실행 및 검증
        assertAll(
                () -> settlementService.requestSettlement(testData.user1.getId(), testData.travel.getId()),
                () -> verify(travelMemberRepository, times(1))
                        .findByTravelIdAndUserId(testData.travel.getId(), testData.user1.getId()),
                () -> verify(travelMemberRepository, times(2))
                        .findAllByTravelId(testData.travel.getId()),
                () -> verify(travelMemberRepository, times(2)).findAllByTravelId(testData.travel.getId()),
                () -> verify(settlementRequestRepository, times(1))
                        .save(any(SettlementRequest.class)),
                () -> verify(travelMemberSettlementHistoryRepository, times(2))
                        .save(any(TravelMemberSettlementHistory.class)),
                () -> verify(settlementRequestLogRepository, times(2)).save(any(SettlementRequestLog.class)),
                () -> verify(taskScheduler, times(1)).schedule(any(Runnable.class), any(Instant.class)));
    }

    @Test
    void 정산_동의를_할_수_있다() {
        TestData testData = SettlementTestFixture.createTestData();
        testData.travel.updateSettlementStatus(SettlementStatus.ONGOING);

        given(travelRepository.findById(testData.travel.getId())).willReturn(Optional.of(testData.travel));
        given(travelMemberRepository.findByTravelIdAndUserId(testData.travel.getId(), testData.user1.getId()))
                .willReturn(Optional.of(testData.travelMember1));

        given(settlementRequestRepository.findByTravelId(testData.travel.getId())).willReturn(Optional.ofNullable(testData.settlementRequest));

        given(travelMemberSettlementHistoryRepository
                .findTravelMemberSettlementHistoryBySettlementRequestIdAndTravelMemberId(
                        testData.settlementRequest.getId(), testData.travelMember1.getId()))
                .willReturn(testData.settlementHistory1);

        given(userRepository.findById(testData.user1.getId())).willReturn(Optional.of(testData.user1));

        assertAll(
                () -> settlementService.agreeSettlement(testData.user1.getId(), testData.travel.getId()),
                () -> verify(travelMemberRepository, times(1)).findByTravelIdAndUserId(testData.user1.getId(), testData.travel.getId()),
                () -> verify(settlementRequestRepository, times(1)).findByTravelId(testData.travel.getId()),
                () -> verify(travelRepository, times(1)).findById(testData.travel.getId()),
                () -> verify(travelMemberSettlementHistoryRepository, times(1))
                        .findTravelMemberSettlementHistoryBySettlementRequestIdAndTravelMemberId(
                                testData.settlementRequest.getId(), testData.travelMember1.getId())
        );

    }

    @Test
    void 정산_취소를_할_수_있다() {
        TestData testData = SettlementTestFixture.createTestData();
        testData.travel.updateSettlementStatus(SettlementStatus.ONGOING);

        given(travelRepository.findById(testData.travel.getId())).willReturn(Optional.of(testData.travel));

        given(settlementRequestRepository.findByTravelId(testData.travel.getId())).willReturn(Optional.ofNullable(testData.settlementRequest));

        given(travelMemberRepository.findAllByTravelId(testData.travel.getId())).willReturn(List.of(testData.travelMember1, testData.travelMember2));

        given(settlementRequestLogRepository.save(any(SettlementRequestLog.class)))
                .willReturn(SettlementRequestLog.builder().user(testData.user2).travel(testData.travel).build());

        assertAll(
                () -> settlementService.cancelSettlement(testData.user1.getId(), testData.travel.getId()),
                () -> verify(settlementRequestRepository, times(1)).findByTravelId(testData.travel.getId()),
                () -> verify(travelRepository, times(1)).findById(testData.travel.getId()),
                () -> verify(travelMemberRepository, times(2)).findAllByTravelId(testData.travel.getId()),
                () -> verify(settlementRequestRepository, times(1)).deleteById(testData.settlementRequest.getId()),
                () -> verify(settlementRequestLogRepository, times(2)).save(any(SettlementRequestLog.class)));
    }
 }