package pl.konradchrzanowski.currencies.service.client.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class CurrencyDTO {

    private final String name;
    private final String currency;
    private final LocalDateTime date;
    private final BigDecimal value;
}
