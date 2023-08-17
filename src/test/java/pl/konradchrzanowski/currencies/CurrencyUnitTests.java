package pl.konradchrzanowski.currencies;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.konradchrzanowski.currencies.domain.Currency;
import pl.konradchrzanowski.currencies.exception.CurrencyMismatchException;
import pl.konradchrzanowski.currencies.exception.NameNotExistsException;
import pl.konradchrzanowski.currencies.exception.constant.ErrorMessages;
import pl.konradchrzanowski.currencies.payload.CurrencyRequest;
import pl.konradchrzanowski.currencies.payload.CurrencyValueResponse;
import pl.konradchrzanowski.currencies.repository.CurrencyRepository;
import pl.konradchrzanowski.currencies.service.client.impl.CurrencyServiceImpl;
import pl.konradchrzanowski.currencies.service.client.mapper.CurrencyMapper;
import pl.konradchrzanowski.currencies.service.nbp.NbpService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyUnitTests {

    private static final String DEFAULT_NAME = "first name";
    private static final String GOOD_CURRENCY_CODE = "PLN";
    private static final String BAD_CURRENCY_CODE = "AAA";
    private static final String TO_SHORT_PATTERN_CURRENCY_CODE = "AA";
    private static final String TO_LONG_PATTERN_CURRENCY_CODE = "AAAA";
    private static final String WITH_DIGIT_PATTERN_CURRENCY_CODE = "1AA";

    private static final BigDecimal DEFAULT_VALUE_RESPONSE = new BigDecimal("4.45");
    private static final LocalDateTime DEFAULT_REQUEST_DATE_TIME = LocalDateTime.parse("2023-08-15T12:22");

    private static final String DEFAULT_CURRENCY_TABLE = "A";

    @Mock
    private CurrencyRepository currencyRepository;
    @InjectMocks
    private CurrencyServiceImpl currencyService;
    @Mock
    private NbpService nbpService;
    @Mock
    private CurrencyMapper currencyMapper;


    @Test
    void shouldSaveWithRequestAndReturnGivenCurrencyValue() {
        CurrencyRequest request = createDefaultRequest(GOOD_CURRENCY_CODE, DEFAULT_NAME);
        Currency currency = createDefaultSavedRequest();
        CurrencyValueResponse expectedResponse = createDefaultCurrencyValueResponse();
        when(nbpService.getCurrencyValue(GOOD_CURRENCY_CODE, DEFAULT_CURRENCY_TABLE)).thenReturn(expectedResponse);

        final var actual = currencyService.getCurrentCurrencyValue(request);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedResponse);
        verify(currencyRepository, times(1)).save(currency);
    }

    @Test
    void shouldThrowExceptionWhenGivenNameIsEmpty() {
        CurrencyRequest request = createDefaultRequest(GOOD_CURRENCY_CODE, "");

        NameNotExistsException thrown = assertThrows(NameNotExistsException.class,
                () -> currencyService.getCurrentCurrencyValue(request));

        assertEquals(ErrorMessages.NAME_NOT_EXISTS, thrown.getMessage());
        verify(currencyRepository, never()).save(any(Currency.class));
    }

    @Test
    void shouldThrowExceptionWhenGivenNameIsNull() {
        CurrencyRequest request = createDefaultRequest(GOOD_CURRENCY_CODE, null);

        NameNotExistsException thrown = assertThrows(NameNotExistsException.class,
                () -> currencyService.getCurrentCurrencyValue(request));

        assertEquals(ErrorMessages.NAME_NOT_EXISTS, thrown.getMessage());
        verify(currencyRepository, never()).save(any(Currency.class));
    }

    @Test
    void shouldThrowExceptionWhenGivenCurrencyCodeIsNull() {
        CurrencyRequest request = createDefaultRequest(null, DEFAULT_NAME);

        CurrencyMismatchException thrown = assertThrows(CurrencyMismatchException.class,
                () -> currencyService.getCurrentCurrencyValue(request));

        assertEquals(ErrorMessages.CURRENCY_CODE_IS_NOT_VALID, thrown.getMessage());
        verify(currencyRepository, never()).save(any(Currency.class));
    }

    @Test
    void shouldThrowExceptionWhenGivenCurrencyCodeIsEmpty() {
        CurrencyRequest request = createDefaultRequest(null, DEFAULT_NAME);

        CurrencyMismatchException thrown = assertThrows(CurrencyMismatchException.class,
                () -> currencyService.getCurrentCurrencyValue(request));

        assertEquals(ErrorMessages.CURRENCY_CODE_IS_NOT_VALID, thrown.getMessage());
        verify(currencyRepository, never()).save(any(Currency.class));
    }

    @Test
    void shouldThrowExceptionWhenGivenCurrencyCodeToLong() {
        CurrencyRequest request = createDefaultRequest(TO_LONG_PATTERN_CURRENCY_CODE, DEFAULT_NAME);

        CurrencyMismatchException thrown = assertThrows(CurrencyMismatchException.class,
                () -> currencyService.getCurrentCurrencyValue(request));

        assertEquals(ErrorMessages.CURRENCY_CODE_IS_NOT_VALID, thrown.getMessage());
        verify(currencyRepository, never()).save(any(Currency.class));
    }

    @Test
    void shouldThrowExceptionWhenGivenCurrencyCodeToShort() {
        CurrencyRequest request = createDefaultRequest(TO_SHORT_PATTERN_CURRENCY_CODE, DEFAULT_NAME);

        CurrencyMismatchException thrown = assertThrows(CurrencyMismatchException.class,
                () -> currencyService.getCurrentCurrencyValue(request));

        assertEquals(ErrorMessages.CURRENCY_CODE_IS_NOT_VALID, thrown.getMessage());
        verify(currencyRepository, never()).save(any(Currency.class));
    }

    @Test
    void shouldThrowExceptionWhenGivenCurrencyCodeContainsDigits() {
        CurrencyRequest request = createDefaultRequest(WITH_DIGIT_PATTERN_CURRENCY_CODE, DEFAULT_NAME);

        CurrencyMismatchException thrown = assertThrows(CurrencyMismatchException.class,
                () -> currencyService.getCurrentCurrencyValue(request));

        assertEquals(ErrorMessages.CURRENCY_CODE_IS_NOT_VALID, thrown.getMessage());
        verify(currencyRepository, never()).save(any(Currency.class));
    }

    @Test
    void shouldThrowExceptionWhenGivenCurrencyCodeIsNotOfficialCode() {
        CurrencyRequest request = createDefaultRequest(BAD_CURRENCY_CODE, DEFAULT_NAME);

        CurrencyMismatchException thrown = assertThrows(CurrencyMismatchException.class,
                () -> currencyService.getCurrentCurrencyValue(request));

        assertEquals(ErrorMessages.CURRENCY_CODE_IS_OFFICIAL, thrown.getMessage());
        verify(currencyRepository, never()).save(any(Currency.class));
    }


    private CurrencyValueResponse createDefaultCurrencyValueResponse() {
        return CurrencyValueResponse.builder().value(DEFAULT_VALUE_RESPONSE).build();
    }

    private CurrencyRequest createDefaultRequest(String currencyCode, String name) {
        return CurrencyRequest.builder()
                .currency(currencyCode)
                .name(name).build();
    }

    private Currency createDefaultSavedRequest() {
        Currency currency = new Currency();
        currency.setCurrency(GOOD_CURRENCY_CODE);
        currency.setName(DEFAULT_NAME);
        currency.setValue(DEFAULT_VALUE_RESPONSE);
        currency.setDate(DEFAULT_REQUEST_DATE_TIME);
        return currencyRepository.save(currency);
    }
}
