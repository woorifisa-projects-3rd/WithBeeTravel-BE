package withbeetravel.domain;

import lombok.Getter;

import java.text.DecimalFormat;

public enum LogTitle {
    PAYMENT_REQUEST("결제 내역 정리 요청", "{0}의 여행이 끝났어요! 🚗💨<br /> 함께 사용한 비용들을 정리해 볼까요? 공동 결제 내역을 확인하고 마무리해 주세요."),
    SETTLEMENT_REQUEST("정산 요청", "{0}에서 정산 요청을 보냈어요! 💸<br /> 함께한 비용을 확인하고, 나의 몫을 정산해 주세요."),
    SETTLEMENT_PENDING("정산 보류", "{0}의 정산이 보류됐어요! 💸<br /> 그룹원들의 잔액이 부족한 상태에요. 계좌에 돈을 입금하고, 다시 한 번 정산을 요청해 주세요. 😊"),
    SETTLEMENT_CANCEL("정산 취소", "{0}의 정산 요청이 취소되었습니다. 😌"),
    SETTLEMENT_RE_REQUEST("정산 재요청", "{0}의 정산 요청에 아직 동의하지 않았어요! 😅<br /> 혹시 잊으신 건 아닌가요? 빠르게 정산을 완료해 주세요."),
    SETTLEMENT_COMPLETE("정산 완료", "{0}의 정산이 완료되었습니다! 🎉<br /> 위비가 {1}원을 지원했습니다.🍀 위비와 함께하는 다음 여행도 기대해요!");

    @Getter
    private final String title;
    private final String messageTemplate;

    LogTitle(String title, String messageTemplate) {
        this.title = title;
        this.messageTemplate = messageTemplate;
    }

    public String getMessage(String travelName) {
        return messageTemplate.replace("{0}", travelName);
    }

    public String getMessage(String travelName, int additionalValue) {
        return messageTemplate
                .replace("{0}", travelName)
                .replace("{1}", formatter.format(additionalValue));
    }

    DecimalFormat formatter = new DecimalFormat("###,###");
}
