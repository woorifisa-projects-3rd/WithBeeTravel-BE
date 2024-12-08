package withbeetravel.support;

import withbeetravel.domain.*;

public class SettlementTestFixture {

    public static TestData createTestData() {
        // User 생성
        User user1 = UserFixture.builder().id(1L).build();
        User user2 = UserFixture.builder().id(2L).build();

        // Account 생성
        Account account1 = AccountFixture.builder().user(user1).isConnectedWibeeCard(true).build();
        Account account2 = AccountFixture.builder().user(user2).isConnectedWibeeCard(false).build();

        user1.updateConnectedAccount(account1);
        user1.updateWibeeCardAccount(account1);
        user2.updateConnectedAccount(account2);

        // Travel 및 TravelMember 생성
        Travel travel = TravelFixture.builder().id(1L).build();
        TravelMember travelMember1 = TravelMember.builder().id(1L).travel(travel).user(user1).isCaptain(true).build();
        TravelMember travelMember2 = TravelMember.builder().id(2L).travel(travel).user(user2).isCaptain(false).build();

        // SharedPayment 생성
        SharedPayment sharedPayment1 = SharedPaymentFixture.builder()
                .id(1L).addedByMember(travelMember1).travel(travel).paymentAmount(70000).build();
        SharedPayment sharedPayment2 = SharedPaymentFixture.builder()
                .id(2L).addedByMember(travelMember2).travel(travel).paymentAmount(40000).build();

        // SettlementRequest 및 Log 생성
        SettlementRequest settlementRequest = SettlementRequestFixture.builder().id(1L).travel(travel).disagreeCount(2).build();
        SettlementRequestLog settlementRequestLog1 = SettlementRequestLogFixture.builder().id(1L).travel(travel).user(user1).build();
        SettlementRequestLog settlementRequestLog2 = SettlementRequestLogFixture.builder().id(2L).travel(travel).user(user2).build();

        // PaymentParticipatedMember 생성
        PaymentParticipatedMember pppm1 = PaymentParticipatedMember.builder().id(1L)
                .travelMember(travelMember1).sharedPayment(sharedPayment1).build();
        PaymentParticipatedMember pppm2 = PaymentParticipatedMember.builder().id(2L)
                .travelMember(travelMember1).sharedPayment(sharedPayment2).build();
        PaymentParticipatedMember pppm3 = PaymentParticipatedMember.builder().id(3L)
                .travelMember(travelMember2).sharedPayment(sharedPayment1).build();
        PaymentParticipatedMember pppm4 = PaymentParticipatedMember.builder().id(4L)
                .travelMember(travelMember2).sharedPayment(sharedPayment2).build();

        // TravelMemberSettlementHistory 생성
        TravelMemberSettlementHistory settlementHistory1 = TravelMemberSettlementHistory.builder()
                .id(1L)
                .settlementRequest(settlementRequest)
                .travelMember(travelMember1)
                .ownPaymentCost(70000)
                .actualBurdenCost(55000)
                .isAgreed(false)
                .build();

        TravelMemberSettlementHistory settlementHistory2 = TravelMemberSettlementHistory.builder()
                .id(2L)
                .settlementRequest(settlementRequest)
                .travelMember(travelMember2)
                .ownPaymentCost(40000)
                .actualBurdenCost(55000)
                .isAgreed(false)
                .build();

        // TestData 객체 생성 및 반환
        return new TestData(
                user1, user2, account1, account2, travel, travelMember1, travelMember2,
                sharedPayment1, sharedPayment2, settlementRequest,
                settlementRequestLog1, settlementRequestLog2, pppm1, pppm2, pppm3, pppm4,
                settlementHistory1, settlementHistory2
        );
    }
}
