package pl.konradchrzanowski.currencies.service.client.mapper;

import org.mapstruct.Mapper;
import pl.konradchrzanowski.currencies.domain.Currency;
import pl.konradchrzanowski.currencies.service.client.dto.CurrencyDTO;

@Mapper(componentModel = "spring", uses = {})
public interface CurrencyMapper extends EntityMapper<CurrencyDTO, Currency> {

    default Currency fromId(Long id) {
        if(id == null) {
            return null;
        }
        Currency currency = new Currency();
        currency.setId(id);
        return currency;
    }
}
