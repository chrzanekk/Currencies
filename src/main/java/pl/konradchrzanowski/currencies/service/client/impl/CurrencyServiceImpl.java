package pl.konradchrzanowski.currencies.service.client.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.konradchrzanowski.currencies.payload.CurrencyRequest;
import pl.konradchrzanowski.currencies.payload.CurrencyValueResponse;
import pl.konradchrzanowski.currencies.service.client.CurrencyService;
import pl.konradchrzanowski.currencies.service.client.dto.CurrencyDTO;
import pl.konradchrzanowski.currencies.service.nbp.NbpService;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final NbpService nbpService;

    private final Logger log = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    public CurrencyServiceImpl(NbpService nbpService) {
        this.nbpService = nbpService;
    }

    @Override
    public CurrencyDTO getCurrentCurrencyValue(CurrencyRequest request) {
        CurrencyValueResponse response = nbpService.getCurrencyValue(request.getCurrency(), null);
        return CurrencyDTO.builder().value(response.getValue()).build();
    }

    @Override
    public List<CurrencyDTO> getAllSavedRequests() {
        return null;
    }
}
