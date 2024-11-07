package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id", nullable = false)
    private Long id;

    @Column(name = "unit", nullable = false)
    private String unit;

    protected Currency() {}

    public Currency(Long id, String unit) {
        this.id = id;
        this.unit = unit;
    }
}
