package pl.konradchrzanowski.currencies.payload;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class CurrencyRequest {
    private final String currency;
    private final String name;
}
