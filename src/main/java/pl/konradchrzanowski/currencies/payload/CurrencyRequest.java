package pl.konradchrzanowski.currencies.payload;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class CurrencyRequest {
    private String currency;
    private String name;
}
