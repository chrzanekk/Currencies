package pl.konradchrzanowski.currencies.service.nbp.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.konradchrzanowski.currencies.payload.CurrencyValueResponse;
import pl.konradchrzanowski.currencies.service.nbp.NbpService;

@Service
public class NbpServiceImpl implements NbpService {

    @Value("${currencies.baseApiUrl}")
    private String baseUrl;

    private final WebClient webClient;

    public NbpServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public CurrencyValueResponse getCurrencyValue(String currencyCode, String table) {
        return webClient.get()
                .uri(uriCreator(currencyCode, table))
                .retrieve().bodyToMono(CurrencyValueResponse.class).block();
    }

    private String uriCreator(String currencyCode, String table) {
        if (table != null) {
            return baseUrl + "rates/" + table + "/" + currencyCode;
        } else {
            return baseUrl + "rates/a/" + currencyCode;
        }
    }
}
