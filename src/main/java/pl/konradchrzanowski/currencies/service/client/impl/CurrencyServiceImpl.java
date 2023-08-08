package pl.konradchrzanowski.currencies.service.client.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.konradchrzanowski.currencies.domain.Currency;
import pl.konradchrzanowski.currencies.domain.enumeration.CurrencyCodes;
import pl.konradchrzanowski.currencies.payload.CurrencyRequest;
import pl.konradchrzanowski.currencies.payload.CurrencyValueResponse;
import pl.konradchrzanowski.currencies.repository.CurrencyRepository;
import pl.konradchrzanowski.currencies.service.client.CurrencyService;
import pl.konradchrzanowski.currencies.service.client.dto.CurrencyDTO;
import pl.konradchrzanowski.currencies.service.client.filter.CurrencyFilter;
import pl.konradchrzanowski.currencies.service.client.filter.CurrencySpecification;
import pl.konradchrzanowski.currencies.service.client.mapper.CurrencyMapper;
import pl.konradchrzanowski.currencies.service.nbp.NbpService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

    private final NbpService nbpService;
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    private final Logger log = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    public CurrencyServiceImpl(NbpService nbpService, CurrencyRepository currencyRepository,
                               CurrencyMapper currencyMapper) {
        this.nbpService = nbpService;
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    @Override
    public CurrencyValueResponse getCurrentCurrencyValue(CurrencyRequest request) {
        boolean isCurrencyCodeValid = isCurrencyCodeValid(request.getCurrency());
        if (isCurrencyCodeValid) {
            log.debug("Request to get current currency value.");
            CurrencyValueResponse result = nbpService.getCurrencyValue(request.getCurrency(), "A");
            CurrencyDTO requestToSave = CurrencyDTO.builder()
                    .name(request.getName())
                    .currency(request.getCurrency())
                    .value(result.getValue())
                    .date(LocalDateTime.now()).build();
            currencyRepository.save(currencyMapper.toEntity(requestToSave));
            return result;
        } else {
            throw new IllegalArgumentException("Currency code is not valid");
        }
    }

    private boolean isCurrencyCodeValid(String currency) {
        Set<String> currenciesCodes = CurrencyCodes.values;
        return currenciesCodes.contains(currency);
    }

    @Override
    public Page<CurrencyDTO> getAllSavedRequests(CurrencyFilter currencyFilter, Pageable pageable) {
        log.debug("Request to get all saved currencies requests.");
        Specification<Currency> specification = CurrencySpecification.createSpecification(currencyFilter);
        return currencyRepository.findAll(specification, pageable).map(currencyMapper::toDto);
    }
}
