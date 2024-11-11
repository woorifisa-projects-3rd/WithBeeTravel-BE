package withbeetravel.service.banking;

import withbeetravel.dto.banking.account.HistoryResponse;

import java.util.List;

public interface HistoryService {
    List<HistoryResponse> showAll(Long AccountId);
}
