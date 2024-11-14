package withbeetravel.service.settlement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import withbeetravel.domain.TravelMember;
import withbeetravel.dto.settlement.ShowMyTotalPaymentResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.repository.*;


import static withbeetravel.exception.error.SettlementErrorCode.SETTLEMENT_NOT_FOUND;
import static withbeetravel.exception.error.TravelErrorCode.TRAVEL_ACCESS_FORBIDDEN;
import static withbeetravel.exception.error.TravelErrorCode.TRAVEL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService{

    private final TravelRepository travelRepository;
    private final TravelMemberRepository travelMemberRepository;
    private final SettlementRequestRepository settlementRequestRepository;

    @Override
    public ShowMyTotalPaymentResponse getMyTotalPayments(Long userId, Long travelId, Long settlementRequestId) {
        travelRepository.findById(travelId).orElseThrow(() -> new CustomException(TRAVEL_NOT_FOUND));
        TravelMember travelMember = travelMemberRepository.findByUserIdAndTravelId(userId, travelId).orElseThrow(() -> new CustomException(TRAVEL_ACCESS_FORBIDDEN));
        settlementRequestRepository.findById(settlementRequestId).orElseThrow(() -> new CustomException(SETTLEMENT_NOT_FOUND));

        return null;
    }
}
