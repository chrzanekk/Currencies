package pl.konradchrzanowski.currencies.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "currencies-requests")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "currency")
    private String currency;

    @Column(name = "request_date")
    LocalDateTime date;

    @Column(name = "value")
    BigDecimal value;

}
