package withbeetravel.dto.request.account;

import lombok.Getter;

@Getter
public class TransferRequest {

    private Long accountId;
    private int amount;
    private String accountNumber;
    private String rqspeNm;
}
