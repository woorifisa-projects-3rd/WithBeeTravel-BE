package withbeetravel.service.payment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import withbeetravel.domain.Category;
import withbeetravel.domain.PaymentParticipatedMember;
import withbeetravel.domain.SharedPayment;
import withbeetravel.domain.TravelMember;
import withbeetravel.domain.User;
import withbeetravel.dto.request.payment.SharedPaymentSearchRequest;
import withbeetravel.dto.response.payment.SharedPaymentParticipatingMemberResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.repository.SharedPaymentRepository;
import withbeetravel.repository.TravelMemberRepository;
import withbeetravel.support.UserFixture;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SharedPaymentServiceImplTest {

    @Mock
    private SharedPaymentRepository sharedPaymentRepository;

    @Mock
    private TravelMemberRepository travelMemberRepository;

    @InjectMocks
    private SharedPaymentServiceImpl sharedPaymentService;

    @Test
    void getSharedPayments_정상조회() {
        // given
        Long travelId = 1L;
        SharedPaymentSearchRequest request = new SharedPaymentSearchRequest();
        request.setPage(0);
        request.setSortBy("latest");
        request.setStartDate(LocalDate.now().minusDays(7));
        request.setEndDate(LocalDate.now());
        request.setCategory("숙박");

        SharedPayment payment = SharedPayment.builder()
                .id(1L)
                .paymentAmount(100000)
                .category(Category.ACCOMMODATION)
                .build();

        Page<SharedPayment> mockPage = new PageImpl<>(List.of(payment));

        when(sharedPaymentRepository.findAllByTravelIdAndMemberIdAndDateRange(
                any(), any(), any(), any(), any(), any()))
                .thenReturn(mockPage);

        // when
        Page<SharedPayment> result = sharedPaymentService.getSharedPayments(travelId, request);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals(Category.ACCOMMODATION, result.getContent().get(0).getCategory());
    }

    @Test
    void getSharedPayments_결과없음_예외발생() {
        // given
        Long travelId = 1L;
        SharedPaymentSearchRequest request = new SharedPaymentSearchRequest();
        request.setPage(0);
        request.setSortBy("latest");

        when(sharedPaymentRepository.findAllByTravelIdAndMemberIdAndDateRange(
                any(), any(), any(), any(), any(), any()))
                .thenReturn(Page.empty());

        // when & then
        assertThrows(CustomException.class,
                () -> sharedPaymentService.getSharedPayments(travelId, request));
    }

    @Test
    void getParticipatingMembersMap_정상매핑() {
        // given
        User user = UserFixture.builder().id(1L).build();

        TravelMember travelMember = TravelMember.builder()
                .id(1L)
                .user(user)
                .build();

        PaymentParticipatedMember participatedMember = PaymentParticipatedMember.builder()
                .travelMember(travelMember)
                .build();

        SharedPayment payment = SharedPayment.builder()
                .id(1L)
                .build();

        payment.addPaymentParticipatedMember(participatedMember);

        Page<SharedPayment> payments = new PageImpl<>(List.of(payment));

        // when
        Map<Long, List<SharedPaymentParticipatingMemberResponse>> result =
                sharedPaymentService.getParticipatingMembersMap(payments);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(1L));

        List<SharedPaymentParticipatingMemberResponse> memberResponses = result.get(1L);
        assertNotNull(memberResponses);
        assertEquals(1, memberResponses.size());
        assertEquals(1L, memberResponses.get(0).getId());
        assertEquals(1, memberResponses.get(0).getProfileImage());
    }

    @Test
    void getSharedPayments_잘못된카테고리_예외발생() {
        // given
        Long travelId = 1L;
        SharedPaymentSearchRequest request = new SharedPaymentSearchRequest();
        request.setCategory("invalid_category");

        // when & then
        assertThrows(CustomException.class,
                () -> sharedPaymentService.getSharedPayments(travelId, request));
    }
}