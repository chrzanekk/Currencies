package pl.konradchrzanowski.currencies.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.konradchrzanowski.currencies.payload.CurrencyRequest;
import pl.konradchrzanowski.currencies.payload.CurrencyResponse;
import pl.konradchrzanowski.currencies.payload.CurrencyValueResponse;
import pl.konradchrzanowski.currencies.service.client.CurrencyService;
import pl.konradchrzanowski.currencies.service.client.dto.CurrencyDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/currencies")
public class CurrencyController {

    private final Logger log = LoggerFactory.getLogger(CurrencyController.class);

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @PostMapping(path = "/get-current-currency-value-command")
    public ResponseEntity<CurrencyValueResponse> getCurrentCurrencyValue(@RequestBody CurrencyRequest currencyRequest) {
        log.debug("REST request to get current currency value: {}", currencyRequest.getCurrency());
        CurrencyDTO result = currencyService.getCurrentCurrencyValue(currencyRequest);
        CurrencyValueResponse response = CurrencyValueResponse.builder().value(result.getValue()).build();
        return ResponseEntity.ok(response);
    }


    @GetMapping(path = "/requests")
    public ResponseEntity<List<CurrencyResponse>> getAllRequests() {
        log.debug("REST get all currencies requests");
        List<CurrencyDTO> result = currencyService.getAllSavedRequests();
        List<CurrencyResponse> requests = convertDTOtoResponse(result);
        return ResponseEntity.ok(requests);
    }

    private List<CurrencyResponse> convertDTOtoResponse(List<CurrencyDTO> result) {
        if (result.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<CurrencyResponse> requests = new ArrayList<>();
            result.forEach(currencyDTO -> {
                requests.add(CurrencyResponse.builder()
                        .currency(currencyDTO.getCurrency())
                        .name(currencyDTO.getName())
                        .date(currencyDTO.getDate())
                        .value(currencyDTO.getValue()).build());
            });
            return requests;
        }
    }
}
