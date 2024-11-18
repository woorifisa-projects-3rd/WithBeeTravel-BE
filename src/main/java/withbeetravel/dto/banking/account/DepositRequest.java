package withbeetravel.dto.banking.account;

import lombok.Getter;

@Getter
public class DepositRequest {
    private int amount;
    private String rqspeNm;
}
