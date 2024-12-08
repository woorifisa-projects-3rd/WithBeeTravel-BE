package withbeetravel.service.payment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import withbeetravel.domain.*;
import withbeetravel.dto.request.payment.SharedPaymentParticipateRequest;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.PaymentErrorCode;
import withbeetravel.repository.PaymentParticipatedMemberRepository;
import withbeetravel.repository.SharedPaymentRepository;
import withbeetravel.repository.TravelMemberRepository;
import withbeetravel.support.SharedPaymentFixture;
import withbeetravel.support.TravelFixture;
import withbeetravel.support.UserFixture;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SharedPaymentParticipantServiceTest {

    @Mock private TravelMemberRepository travelMemberRepository;
    @Mock private SharedPaymentRepository sharedPaymentRepository;
    @Mock private PaymentParticipatedMemberRepository paymentParticipatedMemberRepository;

    @InjectMocks private SharedPaymentParticipantServiceImpl service;

    @Test
    void 정산_참여_멤버를_정상적으로_변경할_수_있다() {

        // Given
        User user1 = UserFixture.builder().id(1L).build();
        User user2 = UserFixture.builder().id(2L).build();
        User user3 = UserFixture.builder().id(3L).build();

        Long travelId = 1L;
        Travel travel = TravelFixture.builder().id(travelId).build();

        List<TravelMember> travelMembers = Arrays.asList(
                TravelMember.builder().id(1L).travel(travel).user(user1).isCaptain(true).build(),
                TravelMember.builder().id(2L).travel(travel).user(user2).isCaptain(false).build(),
                TravelMember.builder().id(3L).travel(travel).user(user3).isCaptain(false).build()
        );

        Long sharedPaymentId = 1L;
        SharedPayment sharedPayment = SharedPaymentFixture.builder()
                .id(sharedPaymentId)
                .addedByMember(travelMembers.get(0))
                .travel(travel)
                .participantCount(2)
                .build();

        List<PaymentParticipatedMember> existingParticipants = Arrays.asList(
                PaymentParticipatedMember.builder()
                        .id(1L)
                        .travelMember(travelMembers.get(0))
                        .sharedPayment(sharedPayment)
                        .build(),
                PaymentParticipatedMember.builder()
                        .id(2L)
                        .travelMember(travelMembers.get(1))
                        .sharedPayment(sharedPayment)
                        .build()
        );

        List<Long> newParticipantIds = Arrays.asList(1L, 3L);
        SharedPaymentParticipateRequest request = new SharedPaymentParticipateRequest(newParticipantIds);

        given(travelMemberRepository.findAllByTravelId(travelId)).willReturn(travelMembers);
        given(sharedPaymentRepository.findById(sharedPaymentId)).willReturn(Optional.of(sharedPayment));
        given(paymentParticipatedMemberRepository.findAllBySharedPaymentId(sharedPaymentId))
                .willReturn(existingParticipants);

        // When
        service.updateParticipantMembers(travelId, sharedPaymentId, request);

        // Then
        assertEquals(2, sharedPayment.getParticipantCount(), "참여 멤버 수가 올바르게 업데이트되지 않았습니다.");

        // 저장된 참여 멤버 검증
        ArgumentCaptor<PaymentParticipatedMember> captor = ArgumentCaptor.forClass(PaymentParticipatedMember.class);
        verify(paymentParticipatedMemberRepository, times(1)).save(captor.capture());

        List<PaymentParticipatedMember> savedMembers = captor.getAllValues();
        List<Long> savedMemberIds = savedMembers.stream()
                .map(member -> member.getTravelMember().getId())
                .toList();

        assertEquals(1, savedMemberIds.size(), "참여 멤버에 새로 저장된 수가 일치하지 않습니다.");
        assertTrue(savedMemberIds.containsAll(Arrays.asList(3L)), "새로 저장된 멤버가 예상과 다릅니다.");

        // 삭제된 참여 멤버 검증
        verify(paymentParticipatedMemberRepository, times(1)).delete(any(PaymentParticipatedMember.class));
        ArgumentCaptor<PaymentParticipatedMember> deleteCaptor = ArgumentCaptor.forClass(PaymentParticipatedMember.class);
        verify(paymentParticipatedMemberRepository).delete(deleteCaptor.capture());
        assertEquals(2L, deleteCaptor.getValue().getTravelMember().getId(), "삭제된 멤버가 예상과 다릅니다.");
    }

    @Test
    void 여행_멤버가_아닌_사용자가_포함되어_있을_경우_예외를_발생시킨다() {
        // Given
        User user1 = UserFixture.builder().id(1L).build();
        User user2 = UserFixture.builder().id(2L).build();

        Long travelId = 1L;
        Travel travel = TravelFixture.builder().id(travelId).build();

        List<TravelMember> travelMembers = Arrays.asList(
                TravelMember.builder().id(1L).travel(travel).user(user1).isCaptain(true).build(),
                TravelMember.builder().id(2L).travel(travel).user(user2).isCaptain(false).build()
        );

        List<Long> invalidParticipantIds = Arrays.asList(1L, 3L); // 3L은 여행 멤버에 포함되지 않음
        SharedPaymentParticipateRequest request = new SharedPaymentParticipateRequest(invalidParticipantIds);

        given(travelMemberRepository.findAllByTravelId(travelId)).willReturn(travelMembers);

        // When & Then
        CustomException exception = assertThrows(
                CustomException.class,
                () -> service.updateParticipantMembers(travelId, 1L, request)
        );

        assertEquals(PaymentErrorCode.NON_TRAVEL_MEMBER_INCLUDED, exception.getErrorCode());
    }
}