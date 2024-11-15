package withbeetravel.service.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.SettlementStatus;
import withbeetravel.domain.SharedPayment;
import withbeetravel.domain.Travel;
import withbeetravel.dto.response.travel.HoneyCapsuleResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.SettlementErrorCode;
import withbeetravel.exception.error.TravelErrorCode;
import withbeetravel.repository.SharedPaymentRepository;
import withbeetravel.repository.TravelRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HoneyCapsuleServiceImpl implements HoneyCapsuleService{

    private final TravelRepository travelRepository;
    private final SharedPaymentRepository sharedPaymentRepository;


    @Override
    @Transactional(readOnly = true)
    public SuccessResponse<List<HoneyCapsuleResponse>> getHoneyCapsuleList(Long travelId) {

        // travelId에 대한 엔티티 가져오기
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_NOT_FOUND));

        // 여행 정산이 아직 안끝났다면, 허니캡슐에 접근할 수 없음
        if(travel.getSettlementStatus() != SettlementStatus.DONE) {
            throw new CustomException(SettlementErrorCode.SETTLEMENT_NOT_COMPLETED);
        }

        // travelId의 공동 결제 내역 모두 불러오기
        List<SharedPayment> allByTravelId = sharedPaymentRepository.findAllByTravelId(travelId);

        // SharedPayment 리스트를 HoneyCapsuleResponse 리스트로 변환
        List<HoneyCapsuleResponse> honeyCapsuleResponseList = allByTravelId.stream()
                .map(HoneyCapsuleResponse::from)
                .collect(Collectors.toList());

        return SuccessResponse.of(200, "여행 기록 조회 성공", honeyCapsuleResponseList);
    }
}
