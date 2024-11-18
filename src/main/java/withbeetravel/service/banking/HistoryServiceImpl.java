package withbeetravel.service.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import withbeetravel.domain.History;
import withbeetravel.dto.banking.account.HistoryResponse;
import withbeetravel.repository.HistoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    public List<HistoryResponse> showAll(Long accountId) {
        List<History> histories = historyRepository.findByAccountIdOrderByDateDesc(accountId);
        return histories.stream().map(HistoryResponse::from).toList();
    }
}
