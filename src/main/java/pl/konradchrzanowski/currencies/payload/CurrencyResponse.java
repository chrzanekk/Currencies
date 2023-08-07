package pl.konradchrzanowski.currencies.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Builder
public class CurrencyResponse {

    private final String name;
    private final String currency;
    private final LocalDateTime date;
    private final BigDecimal value;
}
