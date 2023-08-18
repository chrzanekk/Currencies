package pl.konradchrzanowski.currencies.service.client.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.konradchrzanowski.currencies.domain.Currency;
import pl.konradchrzanowski.currencies.domain.enumeration.CurrencyCodesAll;
import pl.konradchrzanowski.currencies.domain.enumeration.CurrencyCodesTabelA;
import pl.konradchrzanowski.currencies.exception.CurrencyMismatchException;
import pl.konradchrzanowski.currencies.exception.NameNotExistsException;
import pl.konradchrzanowski.currencies.exception.constant.ErrorMessages;
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
import java.util.List;

@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

    public static final int CORRECT_CURRENCY_CODE_LENGTH = 3;
    private static final String DEFAULT_CURRENCY_TABLE = "A";

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
        log.debug("Request to get current currency value.");
        validateCurrencyRequest(request);
        CurrencyValueResponse result = nbpService.getCurrencyValue(request.getCurrency(), DEFAULT_CURRENCY_TABLE);
        CurrencyDTO requestToSave = CurrencyDTO.builder()
                .name(request.getName())
                .currency(request.getCurrency())
                .value(result.getValue())
                .date(LocalDateTime.now()).build();
        currencyRepository.save(currencyMapper.toEntity(requestToSave));
        return result;
    }

    private void validateCurrencyRequest(CurrencyRequest request) {
        validateIsNameIsPresent(request.getName());
        validateIsCurrencyCodeHasCorrectPattern(request);
        validateIsCurrencyCodeValid(request.getCurrency().toUpperCase());
        validateIsCurrencyCodeIsInTableA(request.getCurrency().toUpperCase());
    }

    private void validateIsNameIsPresent(String name) {
        if (name == null || name.isEmpty()) {
            throw new NameNotExistsException(ErrorMessages.NAME_NOT_EXISTS);
        }
    }

    private void validateIsCurrencyCodeIsInTableA(String currencyCode) {
        List<String> currenciesCodes = CurrencyCodesTabelA.codes;
        if (!currenciesCodes.contains(currencyCode)) {
            throw new CurrencyMismatchException(ErrorMessages.CURRENCY_CODE_IS_NOT_FROM_TABLE_A);
        }
    }

    private void validateIsCurrencyCodeValid(String currencyCode) {
        List<String> currenciesCodes = CurrencyCodesAll.codes;
        if (!currenciesCodes.contains(currencyCode)) {
            throw new CurrencyMismatchException(ErrorMessages.CURRENCY_CODE_IS_OFFICIAL);
        }
    }

    private void validateIsCurrencyCodeHasCorrectPattern(CurrencyRequest request) {
        if (isCurrencyCodeNotMatchToRequirements(request)) {
            throw new CurrencyMismatchException(ErrorMessages.CURRENCY_CODE_IS_NOT_VALID);
        }
    }

    private static boolean isCurrencyCodeNotMatchToRequirements(CurrencyRequest request) {
        String currencyCode = request.getCurrency();
        return currencyCode == null
                || currencyCode.isEmpty()
                || isCurrencyCodeContainsAnyDigit(currencyCode)
                || isSizeLowerThanValidCodeLength(currencyCode)
                || isSizeHigherThanValidCodeLength(currencyCode);
    }

    private static boolean isSizeHigherThanValidCodeLength(String currencyCode) {
        return currencyCode.length() > CORRECT_CURRENCY_CODE_LENGTH;
    }

    private static boolean isSizeLowerThanValidCodeLength(String currencyCode) {
        return currencyCode.length() < CORRECT_CURRENCY_CODE_LENGTH;
    }

    private static boolean isCurrencyCodeContainsAnyDigit(String currencyCode) {
        for (int i = 0; i < currencyCode.length(); i++) {
            if (Character.isDigit(currencyCode.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Page<CurrencyDTO> getAllSavedRequests(CurrencyFilter currencyFilter, Pageable pageable) {
        log.debug("Request to get all saved currencies requests.");
        Specification<Currency> specification = CurrencySpecification.createSpecification(currencyFilter);
        return currencyRepository.findAll(specification, pageable).map(currencyMapper::toDto);
    }
}
