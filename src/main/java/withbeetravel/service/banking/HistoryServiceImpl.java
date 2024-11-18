package withbeetravel.service.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import withbeetravel.domain.History;
import withbeetravel.dto.banking.account.HistoryResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.repository.HistoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    public SuccessResponse<List<HistoryResponse>> showAll(Long accountId) {
        List<History> histories = historyRepository.findByAccountIdOrderByDateDesc(accountId);
        List<HistoryResponse> historyResponses = histories.stream().map(HistoryResponse::from).toList();
        return SuccessResponse.of(
                HttpStatus.OK.value(),
                "계좌 거래내역 조회 성공",
                historyResponses
        );
    }
}
