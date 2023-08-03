package pl.konradchrzanowski.currencies.service;

import pl.konradchrzanowski.currencies.payload.CurrencyRequest;
import pl.konradchrzanowski.currencies.payload.CurrencyResponse;
import pl.konradchrzanowski.currencies.service.dto.CurrencyDTO;

import java.util.List;

public interface CurrencyService {

    CurrencyResponse getCurrentCurrencyValue(CurrencyRequest request);

    List<CurrencyDTO> getAllSavedRequests();
}
