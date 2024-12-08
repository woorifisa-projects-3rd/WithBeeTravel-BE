package withbeetravel.support;

import withbeetravel.domain.*;

import java.time.LocalDateTime;

public class SharedPaymentFixture {

    private Long id;
    private TravelMember addedByMember;
    private Travel travel;
    private CurrencyUnit currencyUnit = CurrencyUnit.KRW;
    private int paymentAmount = 100000;
    private Double foreignPaymentAmount = null;
    private Double exchangeRate = null;
    private String paymentComment = "맛있는 저녁";
    private String paymentImage = null;
    private boolean isManuallyAdded = false;
    private int participantCount = 2;
    private Category category = Category.FOOD;
    private String storeName = "이선생 짜글이";
    private LocalDateTime paymentDate = LocalDateTime.now();

    public static SharedPaymentFixture builder() {
        return new SharedPaymentFixture();
    }

    public SharedPaymentFixture id(Long id) {
        this.id = id;
        return this;
    }

    public SharedPaymentFixture addedByMember(TravelMember addedByMember) {
        this.addedByMember = addedByMember;
        return this;
    }

    public SharedPaymentFixture travel(Travel travel) {
        this.travel = travel;
        return this;
    }

    public SharedPaymentFixture currencyUnit(CurrencyUnit currencyUnit) {
        this.currencyUnit = currencyUnit;
        return this;
    }

    public SharedPaymentFixture paymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
        return this;
    }

    public SharedPaymentFixture foreignPaymentAmount(Double foreignPaymentAmount) {
        this.foreignPaymentAmount = foreignPaymentAmount;
        return this;
    }

    public SharedPaymentFixture exchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public SharedPaymentFixture paymentComment(String paymentComment) {
        this.paymentComment = paymentComment;
        return this;
    }

    public SharedPaymentFixture paymentImage(String paymentImage) {
        this.paymentImage = paymentImage;
        return this;
    }

    public SharedPaymentFixture isManuallyAdded(boolean isManuallyAdded) {
        this.isManuallyAdded = isManuallyAdded;
        return this;
    }

    public SharedPaymentFixture participantCount(int participantCount) {
        this.participantCount = participantCount;
        return this;
    }

    public SharedPaymentFixture category(Category category) {
        this.category = category;
        return this;
    }

    public SharedPaymentFixture storeName(String storeName) {
        this.storeName = storeName;
        return this;
    }

    public SharedPaymentFixture paymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public SharedPayment build() {
        return SharedPayment.builder()
                .id(id)
                .addedByMember(addedByMember)
                .travel(travel)
                .currencyUnit(currencyUnit)
                .paymentAmount(paymentAmount)
                .foreignPaymentAmount(foreignPaymentAmount)
                .exchangeRate(exchangeRate)
                .paymentComment(paymentComment)
                .paymentImage(paymentImage)
                .isManuallyAdded(isManuallyAdded)
                .participantCount(participantCount)
                .category(category)
                .storeName(storeName)
                .paymentDate(paymentDate)
                .build();
    }
}
