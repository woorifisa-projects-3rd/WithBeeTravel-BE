package withbeetravel.service.banking;

import withbeetravel.dto.response.account.HistoryResponse;
import withbeetravel.dto.response.SuccessResponse;

import java.util.List;

public interface HistoryService {
    SuccessResponse<List<HistoryResponse>> showAll(Long AccountId);
}
