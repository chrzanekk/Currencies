package pl.konradchrzanowski.currencies.service.nbp;

import pl.konradchrzanowski.currencies.payload.CurrencyValueResponse;

public interface NbpService {
    CurrencyValueResponse getCurrencyValue(String currencyCode, String table);
}
