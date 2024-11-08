package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "shared_payments")
public class SharedPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shared_payment_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by_member_id", nullable = false)
    private TravelMember addedByMember;

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
    private String profileImage;

    @Column(name = "is_manually_added", nullable = false)
    private int isManuallyAdded;

    @Column(name = "is_all_members_participated", nullable = false)
    private int isAllMembersParticipated;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    protected SharedPayment() {}

    @Builder
    public SharedPayment(Long id,
                         TravelMember addedByMember,
                         CurrencyUnit currencyUnit,
                         int paymentAmount,
                         double foreignPaymentAmount,
                         double exchangeRate,
                         String paymentComment,
                         String profileImage,
                         int isManuallyAdded,
                         int isAllMembersParticipated,
                         Category category,
                         String storeName,
                         LocalDateTime paymentDate) {
        this.id = id;
        this.addedByMember = addedByMember;
        this.currencyUnit = currencyUnit;
        this.paymentAmount = paymentAmount;
        this.foreignPaymentAmount = foreignPaymentAmount;
        this.exchangeRate = exchangeRate;
        this.paymentComment = paymentComment;
        this.profileImage = profileImage;
        this.isManuallyAdded = isManuallyAdded;
        this.isAllMembersParticipated = isAllMembersParticipated;
        this.category = category;
        this.storeName = storeName;
        this.paymentDate = paymentDate;
    }
}
