package withbeetravel.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.PaymentParticipatedMember;
import withbeetravel.domain.SharedPayment;
import withbeetravel.domain.TravelMember;
import withbeetravel.dto.request.payment.SharedPaymentParticipateRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.PaymentErrorCode;
import withbeetravel.repository.PaymentParticipatedMemberRepository;
import withbeetravel.repository.SharedPaymentRepository;
import withbeetravel.repository.TravelMemberRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SharedPaymentParticipantServiceImpl implements SharedPaymentParticipantService {
    
    private final TravelMemberRepository travelMemberRepository;
    private final SharedPaymentRepository sharedPaymentRepository;
    private final PaymentParticipatedMemberRepository paymentParticipatedMemberRepository;
    
    @Override
    @Transactional
    public SuccessResponse updateParticipantMembers(
            Long travelId,
            Long sharedPaymentId,
            SharedPaymentParticipateRequest sharedPaymentParticipateRequest
    ) {
        // 여행 멤버 리스트 가져오기
        List<TravelMember> allByTravelId =
                travelMemberRepository.findAllByTravelId(travelId);

        // 수정할 멤버 리스트
        List<Long> newParticipateMembersId = sharedPaymentParticipateRequest.getTravelMembersId();

        // 입력으로 들어온 멤버 리스트 중에 여행 멤버가 아닌 travelMemberId가 있나 검사
        isAllMemberIdInTravelMemberId(allByTravelId, newParticipateMembersId);

        // 공동 결제 내역 가져오기
        SharedPayment sharedPayment = sharedPaymentRepository.findById(sharedPaymentId)
                .orElseThrow(() -> new CustomException(PaymentErrorCode.SHARED_PAYMENT_NOT_FOUND));


        // 전체 인원으로 설정한다면 참여멤버수만 바꿔주기
        if(newParticipateMembersId.size() == allByTravelId.size()) {

            //

            // 정산 인원 수정
            sharedPayment.updateParticipantCount(newParticipateMembersId.size());

            return SuccessResponse.of(HttpStatus.OK.value(), "정산인원 변경 성공");
        }

        // 기존 정산 인원 불러오기
        List<PaymentParticipatedMember> allBySharedPaymentId =
                paymentParticipatedMemberRepository.findAllBySharedPaymentId(sharedPaymentId);

        // 지울 멤버 지우고, 추가할 멤버 추가
        addOrRemoveParticipateMembers(sharedPayment, allByTravelId, newParticipateMembersId, allBySharedPaymentId);

        // 정산 인원 수정
        sharedPayment.updateParticipantCount(newParticipateMembersId.size());

        return SuccessResponse.of(HttpStatus.OK.value(), "정산인원 변경 성공");
    }

    void isAllMemberIdInTravelMemberId(List<TravelMember> allByTravelId, List<Long> newParticipateMembersId) {

        for (int i = 0; i < newParticipateMembersId.size(); i++) {

            boolean flag = false;

            for (int j = 0; j < allByTravelId.size(); j++) {

                if(newParticipateMembersId.get(i).equals(allByTravelId.get(j).getId())) {
                    flag = true;
                    break;
                }
            }

            // 여행 멤버에 포함되지 않은 travelMemberId 발견
            if(!flag) throw new CustomException(PaymentErrorCode.NON_TRAVEL_MEMBER_INCLUDED);
        }

    }

    void addOrRemoveParticipateMembers(
            SharedPayment sharedPayment,
            List<TravelMember> allByTravelId,
            List<Long> newParticipateMembersId,
            List<PaymentParticipatedMember> allBySharedPaymentId
    ) {
        // 두 리스트 정렬
        Collections.sort(newParticipateMembersId);
        Collections.sort(allBySharedPaymentId,
                (o1, o2) -> o1.getTravelMember().getId() < o2.getTravelMember().getId() ? 0 : 1);

        // 투 포인터
        int p1 = 0, p2 = 0;

        while(p1 < newParticipateMembersId.size() && p2 < allBySharedPaymentId.size()) {

            // 이미 존재하는 멤버인 경우
            if(newParticipateMembersId.get(p1).equals(allBySharedPaymentId.get(p2).getTravelMember().getId())) {
                p1++;
                p2++;
            }
            // 새로 추가해야 하는 멤버를 찾은 경우
            else if(newParticipateMembersId.get(p1) < allBySharedPaymentId.get(p2).getTravelMember().getId()) {
                PaymentParticipatedMember newPaymentParticipatedMember =
                        PaymentParticipatedMember.builder()
                                .sharedPayment(sharedPayment)
                                .travelMember(findTravelMemberByTravelMemberId(allByTravelId, newParticipateMembersId.get(p1)))
                                .build();
                paymentParticipatedMemberRepository.save(newPaymentParticipatedMember);
                p1++;
            }
            // 삭제해야 하는 멤버를 찾은 경우
            else {
                paymentParticipatedMemberRepository.delete(allBySharedPaymentId.get(p2));
                p2++;
            }
        }

        while(p1 < newParticipateMembersId.size()) {
            PaymentParticipatedMember newPaymentParticipatedMember =
                    PaymentParticipatedMember.builder()
                            .sharedPayment(sharedPayment)
                            .travelMember(findTravelMemberByTravelMemberId(allByTravelId, newParticipateMembersId.get(p1)))
                            .build();
            paymentParticipatedMemberRepository.save(newPaymentParticipatedMember);
            p1++;
        }

        while(p2 < allBySharedPaymentId.size()) {
            paymentParticipatedMemberRepository.delete(allBySharedPaymentId.get(p2));
            p2++;
        }
    }

    // 전체 여행 멤버 중, travelMemberId에 해당하는 TravelMember 반환
    TravelMember findTravelMemberByTravelMemberId(List<TravelMember> allByTravelId, Long travelMemberId) {

        for (int i = 0; i < allByTravelId.size(); i++) {
            if(allByTravelId.get(i).getId().equals(travelMemberId)) return allByTravelId.get(i);
        }

        // travelMemberId에 해당하는 여행 멤버가 없다면
        throw new CustomException(PaymentErrorCode.NON_TRAVEL_MEMBER_INCLUDED);
    }
}
