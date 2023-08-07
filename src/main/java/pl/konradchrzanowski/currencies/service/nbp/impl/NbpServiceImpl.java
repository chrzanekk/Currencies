package pl.konradchrzanowski.currencies.service.nbp.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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

    @Value("${currencies.baseApiUrl}")
    private String baseUrl;

    private static final String JSON_FORMAT = "?format=json";
    private static final String EXCHANGE_TABLES_PATH = "exchangerates/tables/";

    private final WebClient webClient;

    public NbpServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public CurrencyValueResponse getCurrencyValue(String currencyCode, String table) {
        List<RatesResponse> ratesResponseList = getRateResponsesList(currencyCode, table);
        List<RateResponse> responses = getRateResponseList(ratesResponseList);
        RateResponse result = filterResult(currencyCode, responses);
        return CurrencyValueResponse.builder().value(new BigDecimal(result.getMid())).build();
    }

    private RateResponse filterResult(String currencyCode, List<RateResponse> responses) {
        RateResponse result =
                responses.stream().filter(rateResponse -> rateResponse.getCode().equals(currencyCode.toUpperCase()))
                        .findFirst().orElseThrow(() -> new IllegalArgumentException("Not found"));
        return result;
    }

    private List<RateResponse> getRateResponseList(List<RatesResponse> ratesResponseList) {
        RatesResponse first = ratesResponseList.stream().findFirst().orElseThrow(() -> new IllegalArgumentException(
                "Not found"));
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
