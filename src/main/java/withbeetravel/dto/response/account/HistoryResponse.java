package withbeetravel.dto.response.account;

import lombok.Getter;
import withbeetravel.domain.History;

import java.time.LocalDateTime;

@Getter
public class HistoryResponse {

    private LocalDateTime date;
    private Integer rcvAm;
    private Integer payAm;
    private long balance;
    private String rqspeNm;

    public HistoryResponse(LocalDateTime date, Integer rcvAm,
                           Integer payAm, long balance, String rqspeNm) {
        this.date = date;
        this.rcvAm = rcvAm;
        this.payAm = payAm;
        this.balance = balance;
        this.rqspeNm = rqspeNm;
    }

    public static HistoryResponse from(History history){

        return new HistoryResponse(history.getDate(), history.getRcvAm(),
                history.getPayAM(), history.getBalance(), history.getRqspeNm());
    }
}
