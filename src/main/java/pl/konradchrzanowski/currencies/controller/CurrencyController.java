package pl.konradchrzanowski.currencies.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.konradchrzanowski.currencies.payload.CurrencyRequest;
import pl.konradchrzanowski.currencies.payload.CurrencyValueResponse;
import pl.konradchrzanowski.currencies.service.client.CurrencyService;
import pl.konradchrzanowski.currencies.service.client.dto.CurrencyDTO;
import pl.konradchrzanowski.currencies.service.client.filter.CurrencyFilter;

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
        CurrencyValueResponse response = currencyService.getCurrentCurrencyValue(currencyRequest);
        return ResponseEntity.ok(response);
    }


    @GetMapping(path = "/requests")
    public ResponseEntity<Page<CurrencyDTO>> getAllRequests(
            @RequestBody CurrencyFilter currencyFiler,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        log.debug("REST get all saved currencies requests");

        Pageable pageable = PageRequest.of(page, size);

        Page<CurrencyDTO> result = currencyService.getAllSavedRequests(currencyFiler, pageable);
        return ResponseEntity.ok().body(result);
    }
}
