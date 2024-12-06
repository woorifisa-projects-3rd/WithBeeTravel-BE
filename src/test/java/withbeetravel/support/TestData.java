package withbeetravel.support;

import withbeetravel.domain.*;

public class TestData {
    public final User user1;
    public final User user2;
    public final Account account1;
    public final Account account2;
    public final Travel travel;
    public final TravelMember travelMember1;
    public final TravelMember travelMember2;
    public final SharedPayment sharedPayment1;
    public final SharedPayment sharedPayment2;
    public final SettlementRequest settlementRequest;
    public final SettlementRequestLog settlementRequestLog1;
    public final SettlementRequestLog settlementRequestLog2;
    public final PaymentParticipatedMember pppm1;
    public final PaymentParticipatedMember pppm2;
    public final PaymentParticipatedMember pppm3;
    public final PaymentParticipatedMember pppm4;
    public final TravelMemberSettlementHistory settlementHistory1;
    public final TravelMemberSettlementHistory settlementHistory2;

    public TestData(
            User user1, User user2, Account account1, Account account2,
            Travel travel, TravelMember travelMember1, TravelMember travelMember2,
            SharedPayment sharedPayment1, SharedPayment sharedPayment2,
            SettlementRequest settlementRequest, SettlementRequestLog settlementRequestLog1,
            SettlementRequestLog settlementRequestLog2, PaymentParticipatedMember pppm1,
            PaymentParticipatedMember pppm2, PaymentParticipatedMember pppm3,
            PaymentParticipatedMember pppm4, TravelMemberSettlementHistory settlementHistory1,
            TravelMemberSettlementHistory settlementHistory2) {
        this.user1 = user1;
        this.user2 = user2;
        this.account1 = account1;
        this.account2 = account2;
        this.travel = travel;
        this.travelMember1 = travelMember1;
        this.travelMember2 = travelMember2;
        this.sharedPayment1 = sharedPayment1;
        this.sharedPayment2 = sharedPayment2;
        this.settlementRequest = settlementRequest;
        this.settlementRequestLog1 = settlementRequestLog1;
        this.settlementRequestLog2 = settlementRequestLog2;
        this.pppm1 = pppm1;
        this.pppm2 = pppm2;
        this.pppm3 = pppm3;
        this.pppm4 = pppm4;
        this.settlementHistory1 = settlementHistory1;
        this.settlementHistory2 = settlementHistory2;
    }
}
