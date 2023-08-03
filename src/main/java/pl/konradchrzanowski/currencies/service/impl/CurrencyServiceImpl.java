package pl.konradchrzanowski.currencies.service.impl;

import org.springframework.stereotype.Service;
import pl.konradchrzanowski.currencies.payload.CurrencyRequest;
import pl.konradchrzanowski.currencies.payload.CurrencyResponse;
import pl.konradchrzanowski.currencies.service.CurrencyService;
import pl.konradchrzanowski.currencies.service.dto.CurrencyDTO;

import java.util.List;
@Service
public class CurrencyServiceImpl implements CurrencyService {
    @Override
    public CurrencyResponse getCurrentCurrencyValue(CurrencyRequest request) {
        return null;
    }

    @Override
    public List<CurrencyDTO> getAllSavedRequests() {
        return null;
    }
}
