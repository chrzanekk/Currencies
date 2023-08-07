package pl.konradchrzanowski.currencies.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "currencies_requests")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "currency_code")
    private String currency;

    @Column(name = "request_date")
    private LocalDateTime date;

    @Column(name = "currency_value")
    private BigDecimal value;

}
