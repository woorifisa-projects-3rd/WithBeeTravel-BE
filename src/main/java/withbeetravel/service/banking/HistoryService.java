package withbeetravel.service.banking;

import withbeetravel.dto.request.account.HistoryRequest;
import withbeetravel.dto.response.account.HistoryResponse;
import withbeetravel.dto.response.SuccessResponse;

import java.util.List;

public interface HistoryService {
    SuccessResponse<List<HistoryResponse>> showAll(Long AccountId);

    SuccessResponse addHistory(Long accountId, HistoryRequest historyRequest);
}
