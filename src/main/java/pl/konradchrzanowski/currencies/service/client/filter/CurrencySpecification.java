package pl.konradchrzanowski.currencies.service.client.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.konradchrzanowski.currencies.domain.Currency;

@Component
public class CurrencySpecification {

    public static Specification<Currency> createSpecification(CurrencyFilter currencyFilter) {
        Specification<Currency> specification = Specification.where(null);
        if (currencyFilter != null) {
            if (currencyFilter.getId() != null) {
                specification = specification.and(
                        (root, query, criteriaBuilder)
                                -> criteriaBuilder.equal(root.<Long>get(Currency.Fields.id), currencyFilter.getId()));
            }
            if (currencyFilter.getName() != null) {
                specification = specification.and(
                        (root, query, criteriaBuilder)
                                -> criteriaBuilder.like(root.get(Currency.Fields.name),
                                "%" + currencyFilter.getName() + "%"));
            }
            if (currencyFilter.getCurrency() != null) {
                specification = specification.and(
                        (root, query, criteriaBuilder)
                                -> criteriaBuilder.like(root.get(Currency.Fields.currency),
                                "%" + currencyFilter.getCurrency() + "%"));
            }
            if (currencyFilter.getValueStartsWith() != null) {
                specification = specification.and(
                        (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Currency.Fields.value),
                                currencyFilter.getValueStartsWith()));
            }
            if (currencyFilter.getValueEndsWith() != null) {
                specification = specification.and(
                        (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(Currency.Fields.value),
                                currencyFilter.getValueEndsWith()));
            }
            if (currencyFilter.getDateStartsWith() != null) {
                specification = specification.and(
                        (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Currency.Fields.date),
                                currencyFilter.getDateStartsWith()));
            }
            if (currencyFilter.getDateEndsWith() != null) {
                specification = specification.and(
                        (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(Currency.Fields.date),
                                currencyFilter.getDateEndsWith()));
            }
        }
        return specification;
    }
}
