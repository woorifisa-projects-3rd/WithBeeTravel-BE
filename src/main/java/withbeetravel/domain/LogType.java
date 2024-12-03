package withbeetravel.domain;

public enum LogType {
    REGISTER,//가입 일자
    LOGIN, // 로그인 기록
    LOGIN_FAILED, // 로그인 실패 기록
    TRANSFER,// 송금 기록
    RECEIVE,// 송금 받은 기록
    PAYMENT, // 거래 내역 추가 기록
    DEPOSIT, // 직접 입금 내역 기록
    SETTLEMENT, // 정산 기록
}
