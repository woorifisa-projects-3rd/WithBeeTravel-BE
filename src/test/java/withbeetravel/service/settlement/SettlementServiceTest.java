package withbeetravel.service.settlement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import withbeetravel.domain.*;
import withbeetravel.repository.*;
import withbeetravel.support.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    @InjectMocks private SettlementServiceImpl settlementService;

    @Test
    void 정산_요청을_할_수_있다() {
        User user1 = UserFixture.builder().id(1L).build();
        User user2 = UserFixture.builder().id(2L).build();

        Account account1 = AccountFixture.builder().user(user1).isConnectedWibeeCard(true).build();
        Account account2 = AccountFixture.builder().user(user2).isConnectedWibeeCard(false).build();

        user1.updateConnectedAccount(account1);
        user1.updateWibeeCardAccount(account1);
        user2.updateConnectedAccount(account2);

        Travel travel = TravelFixture.builder().id(1L).build();
        TravelMember travelMember1 = TravelMember.builder().travel(travel).user(user1).isCaptain(true).build();
        TravelMember travelMember2 = TravelMember.builder().travel(travel).user(user1).isCaptain(false).build();

        SharedPayment sharedPayment1 = SharedPaymentFixture.builder()
                .id(1L).addedByMember(travelMember1).travel(travel).build();
        SharedPayment sharedPayment2 = SharedPaymentFixture.builder()
                .id(2L).addedByMember(travelMember2).travel(travel).build();

        SettlementRequest settlementRequest = SettlementRequestFixture.builder().travel(travel).disagreeCount(2).build();

        SettlementRequestLog settlementRequestLog1 =
                SettlementRequestLogFixture.builder().travel(travel).user(user1).build();
        SettlementRequestLog settlementRequestLog2 =
                SettlementRequestLogFixture.builder().travel(travel).user(user2).build();

        given(userRepository.existsById(user1.getId())).willReturn(true);
        given(userRepository.existsById(user2.getId())).willReturn(true);
        given(travelRepository.existsById(travel.getId())).willReturn(true);
        given(travelRepository.findById(travel.getId())).willReturn(Optional.of(travel));
        given(travelMemberRepository.findAllByTravelId(travel.getId()))
                .willReturn(List.of(travelMember1, travelMember2));
        given(sharedPaymentRepository.findAllByTravelId(travel.getId()))
                .willReturn(List.of(sharedPayment1, sharedPayment2));
        given(paymentParticipatedMemberRepository.existsByTravelMemberIdAndSharedPaymentId(
                travelMember1.getId(), sharedPayment1.getId())).willReturn(true);
        given(paymentParticipatedMemberRepository.existsByTravelMemberIdAndSharedPaymentId(
                travelMember1.getId(), sharedPayment2.getId())).willReturn(true);
        given(paymentParticipatedMemberRepository.existsByTravelMemberIdAndSharedPaymentId(
                travelMember2.getId(), sharedPayment1.getId())).willReturn(true);
        given(paymentParticipatedMemberRepository.existsByTravelMemberIdAndSharedPaymentId(
                travelMember2.getId(), sharedPayment2.getId())).willReturn(true);
        given(travelMemberRepository.findByTravelIdAndUserId(travel.getId(), user1.getId()))
                .willReturn(Optional.of(travelMember1));
        given(settlementRequestLogRepository.findAllByTravelId(travel.getId()))
                .willReturn(List.of(settlementRequestLog1, settlementRequestLog2));
        given(settlementRequestRepository.save(any(SettlementRequest.class))).willReturn(settlementRequest);

        assertAll(
                () -> settlementService.requestSettlement(user1.getId(), travel.getId()),
                () -> verify(travelMemberRepository, times(1))
                        .findByTravelIdAndUserId(travel.getId(), user1.getId()),
                () -> verify(settlementRequestRepository, times(1))
                        .save(any(SettlementRequest.class)),
                () -> verify(travelMemberSettlementHistoryRepository, times(2))
                        .save(any(TravelMemberSettlementHistory.class)),
                () -> verify(settlementRequestLogRepository, times(2)).save(any(SettlementRequestLog.class))
        );
    }
}