package withbeetravel.dto.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class HistoryRequest {

    private Integer payAm;
    private String rqspeNm;
    @JsonProperty("isWibeeCard")
    private boolean isWibeeCard;
}
