package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "travel_countries")
public class TravelCountry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_country_id", nullable = false)
    private Long id;

    @Column(name = "country", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Country country;

    @JoinColumn(name = "travel_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Travel travel;

    protected TravelCountry() {}

    @Builder
    public TravelCountry(Long id, Country country, Travel travel) {
        this.id = id;
        this.country = country;
        this.travel = travel;
    }
}
