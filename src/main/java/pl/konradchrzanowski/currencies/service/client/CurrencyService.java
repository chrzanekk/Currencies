package pl.konradchrzanowski.currencies.service.client;

import pl.konradchrzanowski.currencies.payload.CurrencyRequest;
import pl.konradchrzanowski.currencies.service.client.dto.CurrencyDTO;

import java.util.List;

public interface CurrencyService {

    CurrencyDTO getCurrentCurrencyValue(CurrencyRequest request);

    List<CurrencyDTO> getAllSavedRequests();
}
