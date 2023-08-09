package pl.konradchrzanowski.currencies.service.nbp.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.konradchrzanowski.currencies.exception.RatesNotFoundException;
import pl.konradchrzanowski.currencies.payload.CurrencyValueResponse;
import pl.konradchrzanowski.currencies.payload.RateResponse;
import pl.konradchrzanowski.currencies.payload.RatesResponse;
import pl.konradchrzanowski.currencies.service.nbp.NbpService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class NbpServiceImpl implements NbpService {

    public static final String RATES_NOT_FOUND = "Rates not found";
    private static final String EXCHANGE_TABLES_PATH = "exchangerates/tables/";
    private final Logger log = LoggerFactory.getLogger(NbpServiceImpl.class);

    @Value("${currencies.baseApiUrl}")
    private String baseUrl;

    private final WebClient webClient;

    public NbpServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public CurrencyValueResponse getCurrencyValue(String currencyCode, String table) {
        log.debug("Request to get currency value for currency {} from table {}", currencyCode, table);
        List<RatesResponse> ratesResponseList = getRateResponsesList(currencyCode, table);
        List<RateResponse> listOfRates = getRateResponseList(ratesResponseList);
        RateResponse result = findRateByCurrencyCode(currencyCode, listOfRates);
        return CurrencyValueResponse.builder().value(new BigDecimal(result.getMid())).build();
    }

    private RateResponse findRateByCurrencyCode(String currencyCode, List<RateResponse> responses) {
        return responses.stream().filter(rateResponse -> rateResponse.getCode().equals(currencyCode.toUpperCase()))
                .findFirst().orElseThrow(() -> new RatesNotFoundException(RATES_NOT_FOUND));
    }

    private List<RateResponse> getRateResponseList(List<RatesResponse> ratesResponseList) {
        RatesResponse first = ratesResponseList.stream().findFirst().orElseThrow(() -> new RatesNotFoundException(
                RATES_NOT_FOUND));
        return first.getRates();
    }

    private List<RatesResponse> getRateResponsesList(String currencyCode, String table) {
        final RatesResponse[] ratesResponses = getRatesResponses(currencyCode, table);
        if (ratesResponses != null) {
            return Arrays.stream(ratesResponses).toList();
        } else {
            return Collections.emptyList();
        }
    }

    private RatesResponse[] getRatesResponses(String currencyCode, String table) {
        return webClient.get()
                .uri(uriCreator(table))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve().bodyToMono(RatesResponse[].class).block();
    }

    private String uriCreator(String table) {
        if (table != null) {
            return baseUrl + EXCHANGE_TABLES_PATH + table + "/";
        } else {
            return baseUrl + EXCHANGE_TABLES_PATH + "A/";
        }
    }
}
