package pl.konradchrzanowski.currencies.service.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.konradchrzanowski.currencies.payload.CurrencyRequest;
import pl.konradchrzanowski.currencies.payload.CurrencyValueResponse;
import pl.konradchrzanowski.currencies.service.client.dto.CurrencyDTO;
import pl.konradchrzanowski.currencies.service.client.filter.CurrencyFilter;

import java.util.List;

public interface CurrencyService {

    CurrencyValueResponse getCurrentCurrencyValue(CurrencyRequest request);

    List<CurrencyDTO> getAll();
    Page<CurrencyDTO> getAllWithFilter(CurrencyFilter currencyFilter, Pageable pageable);
}
