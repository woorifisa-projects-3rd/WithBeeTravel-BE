package withbeetravel.domain;

import lombok.Getter;

public enum LogTitle {
    PAYMENT_REQUEST("결제 내역 정리 요청", "여행이 끝났어요! 🚗💨 함께 사용한 비용들을 정리해 볼까요? 공동 결제 내역을 확인하고 마무리해 주세요."),
    SETTLEMENT_REQUEST("정산 요청", "{0}에서 정산 요청을 보냈어요! 💸 함께한 비용을 확인하고, 나의 몫을 정산해 주세요."),
    SETTLEMENT_PENDING("정산 보류", "{0}의 정산이 보류됐어요! 💸 그룹원들의 잔액이 부족한 상태에요. 계좌에 돈을 입금하고, 다시 한 번 정산을 요청해 주세요. 😊"),
    SETTLEMENT_CANCEL("정산 취소", "{0}의 정산 요청이 취소되었습니다. 😌"),
    SETTLEMENT_RE_REQUEST("정산 재요청", "아직 정산이 완료되지 않았어요! 😅 혹시 잊으신 건 아닌가요? 빠르게 정산을 완료해 주세요."),
    SETTLEMENT_COMPLETE("정산 완료", "정산이 완료되었습니다! 🎉 모두와 나눠야 할 금액이 처리되었어요. 다음 여행도 기대해요!");

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
}
