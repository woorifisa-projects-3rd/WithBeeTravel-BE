package withbeetravel.dto.request.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardCompletedRequest {

//   카드 발급 없이 계좌 연결 -> wibeeCardAccountId : null, connectedAccountId : 계좌번호
//   카드 발급 계좌 연결 -> wibeeCardAccountId : 계좌 번호, connectedAccountId : 계좌번호
    private Long accountId;
    private boolean isWibeeCard;
}
