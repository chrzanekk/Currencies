package pl.konradchrzanowski.currencies.service.client.filter;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Builder
@Data
public class CurrencyFilter {
    private Long id;
    private String name;
    private String currency;
    private BigDecimal valueStartsWith;
    private BigDecimal valueEndsWith;
    private LocalDateTime dateStartsWith;
    private LocalDateTime dateEndsWith;
}
