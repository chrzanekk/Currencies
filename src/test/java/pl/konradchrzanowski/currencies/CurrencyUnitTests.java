package pl.konradchrzanowski.currencies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.konradchrzanowski.currencies.domain.Currency;
import pl.konradchrzanowski.currencies.repository.CurrencyRepository;
import pl.konradchrzanowski.currencies.service.client.dto.CurrencyDTO;
import pl.konradchrzanowski.currencies.service.client.impl.CurrencyServiceImpl;
import pl.konradchrzanowski.currencies.service.client.mapper.CurrencyMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class CurrencyUnitTests {

    private static final String DEFAULT_NAME = "first name";
    private static final String WRONG_NAME = "wrong name";
    private static final String GOOD_CURRENCY_CODE = "PLN";
    private static final String BAD_CURRENCY_CODE = "AAA";
    private static final String TO_SHORT_PATTERN_CURRENCY_CODE = "AA";
    private static final String TO_LONG_PATTERN_CURRENCY_CODE = "AAAA";
    private static final String WITH_DIGIT_PATTERN_CURRENCY_CODE = "1AA";

    private static final BigDecimal DEFAULT_VALUE_RESPONSE = new BigDecimal("4.45");
    private static final BigDecimal WRONG_VALUE_RESPONSE = new BigDecimal("6.45");
    private static final LocalDateTime DEFAULT_REQUEST_DATE_TIME = LocalDateTime.parse("2023-08-15T12:22");
    private static final LocalDateTime DEFAULT_REQUEST_DATE_TIME_END = LocalDateTime.parse("2023-08-16T12:22");
    private static final LocalDateTime WRONG_REQUEST_DATE_TIME_START = LocalDateTime.parse("2023-08-11T12:22");
    private static final LocalDateTime WRONG_REQUEST_DATE_TIME_END = LocalDateTime.parse("2023-08-14T12:22");

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    private CurrencyMapper currencyMapper;

    private Currency currency;

    @BeforeEach
    public void init() {
        currency = createDefaultSavedRequest();
    }

    private Currency createDefaultSavedRequest() {
        CurrencyDTO currencyDTO = CurrencyDTO.builder()
                .currency(GOOD_CURRENCY_CODE)
                .name(DEFAULT_NAME)
                .value(DEFAULT_VALUE_RESPONSE)
                .date(DEFAULT_REQUEST_DATE_TIME).build();
        return currencyRepository.save(currencyMapper.toEntity(currencyDTO));
    }
}
