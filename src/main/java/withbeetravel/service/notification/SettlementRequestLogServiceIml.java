package withbeetravel.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import withbeetravel.domain.*;
import withbeetravel.dto.request.settlementRequestLog.SettlementRequestLogDto;
import withbeetravel.repository.SettlementRequestLogRepository;
import withbeetravel.repository.TravelMemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementRequestLogServiceIml implements SettlementRequestLogService{

    private final SettlementRequestLogRepository settlementRequestLogRepository;
    private final TravelMemberRepository travelMemberRepository;

    @Override
    public List<SettlementRequestLogDto> getSettlementRequestLogs(Long userId) {

        // 나의 여행 아이디 리스트
        List<Long> travelIds = getTravelIds(userId);

        // 모든 로그 리스트
        List<SettlementRequestLog> settlementRequestLogs = getSettlementRequestLogs(travelIds);

        // settlementRequestLogDto 리스트로 가공 (링크 추가)
        List<SettlementRequestLogDto> settlementRequestLogDtos = new ArrayList<>();
        for (SettlementRequestLog settlementRequestLog : settlementRequestLogs) {
            String link = createLink(settlementRequestLog);
            SettlementRequestLogDto settlementRequestLogDto = createSettlementREquestLogDto(settlementRequestLog, link);
            settlementRequestLogDtos.add(settlementRequestLogDto);
        }

        return settlementRequestLogDtos;
    }

    private SettlementRequestLogDto createSettlementREquestLogDto(SettlementRequestLog settlementRequestLog, String link) {
       return SettlementRequestLogDto.builder().id(settlementRequestLog.getId())
                .logTime(settlementRequestLog.getLogTime())
                .logTitle(settlementRequestLog.getLogTitle().getTitle())
                .logMessage(settlementRequestLog.getLogMessage())
                .link(link)
                .build();
    }

    private List<SettlementRequestLog> getSettlementRequestLogs(List<Long> travelIds) {
        return travelIds.stream()
                .map(settlementRequestLogRepository::findAllByTravelId)
                .flatMap(List::stream)
                .toList();
    }

    private List<Long> getTravelIds(Long userId) {
        return travelMemberRepository.findAllByUserId(userId)
                .stream()
                .map(TravelMember::getTravel)
                .map(Travel::getId)
                .toList();
    }

    private String createLink(SettlementRequestLog settlementRequestLog) {
        String link = null;
        Long travelId = settlementRequestLog.getTravel().getId();
        LogTitle logTitle = settlementRequestLog.getLogTitle();
        if (logTitle.equals(LogTitle.PAYMENT_REQUEST)) {
            link = "travel/" + travelId + "/payments";
        } else if (logTitle.equals(LogTitle.SETTLEMENT_REQUEST) || logTitle.equals(LogTitle.SETTLEMENT_RE_REQUEST)) {
            link = "travel/" + travelId + "/settlement";
        }
        return link;
    }
}
