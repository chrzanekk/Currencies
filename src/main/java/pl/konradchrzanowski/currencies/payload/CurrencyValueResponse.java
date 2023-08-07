package pl.konradchrzanowski.currencies.payload;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CurrencyValueResponse {
    private final BigDecimal value;
}
