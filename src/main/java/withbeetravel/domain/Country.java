package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    protected Country() {}

    @Builder
    public Country(Long id, Currency currency, String countryName) {
        this.id = id;
        this.currency = currency;
        this.countryName = countryName;
    }
}
