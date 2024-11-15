package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "shared_payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SharedPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shared_payment_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by_member_id", nullable = false)
    private TravelMember addedByMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @Column(name = "currency_unit", nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyUnit currencyUnit;

    @Column(name = "payment_amount", nullable = false)
    private int paymentAmount;

    @Column(name = "foreign_payment_amount")
    private double foreignPaymentAmount;

    @Column(name = "exchange_rate")
    private double exchangeRate;

    @Column(name = "payment_comment")
    private String paymentComment;

    @Column(name = "payment_image")
    private String paymentImage;

    @Column(name = "is_manually_added", nullable = false)
    private boolean isManuallyAdded;

    @Column(name = "participant_count", nullable = false)
    private int participantCount;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @OneToMany(mappedBy = "sharedPayment")
    private List<PaymentParticipatedMember> paymentParticipatedMembers = new ArrayList<>();

    @Builder
    public SharedPayment(Long id,
                         TravelMember addedByMember,
                         Travel travel,
                         CurrencyUnit currencyUnit,
                         int paymentAmount,
                         double foreignPaymentAmount,
                         double exchangeRate,
                         String paymentComment,
                         String paymentImage,
                         boolean isManuallyAdded,
                         int participantCount,
                         Category category,
                         String storeName,
                         LocalDateTime paymentDate) {
        this.id = id;
        this.addedByMember = addedByMember;
        this.travel = travel;
        this.currencyUnit = currencyUnit;
        this.paymentAmount = paymentAmount;
        this.foreignPaymentAmount = foreignPaymentAmount;
        this.exchangeRate = exchangeRate;
        this.paymentComment = paymentComment;
        this.paymentImage = paymentImage;
        this.isManuallyAdded = isManuallyAdded;
        this.participantCount = participantCount;
        this.category = category;
        this.storeName = storeName;
        this.paymentDate = paymentDate;
    }

    public void updatePaymentImage(String newPaymentImage) {
        this.paymentImage = newPaymentImage;
    }

    public void updatePaymentCommnet(String newPaymentComment) {
        this.paymentComment = newPaymentComment;
    }
}
