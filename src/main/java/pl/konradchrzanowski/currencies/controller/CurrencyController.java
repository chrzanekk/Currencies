package pl.konradchrzanowski.currencies.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.konradchrzanowski.currencies.controller.util.PaginationUtil;
import pl.konradchrzanowski.currencies.payload.CurrencyRequest;
import pl.konradchrzanowski.currencies.payload.CurrencyValueResponse;
import pl.konradchrzanowski.currencies.service.client.CurrencyService;
import pl.konradchrzanowski.currencies.service.client.dto.CurrencyDTO;
import pl.konradchrzanowski.currencies.service.client.filter.CurrencyFilter;

import java.util.List;

@RestController
@RequestMapping(path = "/currencies")
@CrossOrigin(origins = "http://localhost:4200")
public class CurrencyController {

    private final Logger log = LoggerFactory.getLogger(CurrencyController.class);

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @PostMapping( "/get-current-currency-value-command")
    public ResponseEntity<CurrencyValueResponse> getCurrentCurrencyValue(@RequestBody CurrencyRequest currencyRequest) {
        log.debug("REST request to get current currency value: {}", currencyRequest.getCurrency());
        CurrencyValueResponse response = currencyService.getCurrentCurrencyValue(currencyRequest);
        return ResponseEntity.ok().body(response);
    }
//todo change test for getMapping - add pegable to params
    @GetMapping("/requests")
    public ResponseEntity<List<CurrencyDTO>> getAllRequests(
            CurrencyFilter currencyFiler, Pageable pageable) {
        log.debug("REST get all saved currencies requests");


        Page<CurrencyDTO> result = currencyService.getAllSavedRequests(currencyFiler, pageable);
        HttpHeaders headers =
                PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result);
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }
}
