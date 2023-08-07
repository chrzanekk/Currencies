package pl.konradchrzanowski.currencies.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyValueResponse {
    @JsonProperty("value")
    private BigDecimal value;
}
