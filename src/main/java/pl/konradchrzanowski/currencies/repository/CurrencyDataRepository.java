package pl.konradchrzanowski.currencies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.konradchrzanowski.currencies.domain.Currency;
@Repository
public interface CurrencyDataRepository extends JpaRepository<Currency, Long> {
}
