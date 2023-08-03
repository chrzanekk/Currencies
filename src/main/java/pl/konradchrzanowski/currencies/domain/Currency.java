package pl.konradchrzanowski.currencies.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "currency")
    @NotNull
    private String currency;

    @Column(name = "request_date")
    LocalDateTime date;

    @Column(name = "value")
    @NotNull
    BigDecimal value;

}
