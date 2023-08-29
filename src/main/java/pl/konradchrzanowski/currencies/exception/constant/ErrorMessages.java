package pl.konradchrzanowski.currencies.exception.constant;

public final class ErrorMessages {

    public static final String CURRENCY_CODE_IS_NOT_VALID = "Kod waluty jest niepoprawny lub nie został podany.";
    public static final String CURRENCY_CODE_IS_OFFICIAL = "Podany kod nie jest oficialnym kodem waluty";
    public static final String CURRENCY_CODE_IS_NOT_FROM_TABLE_A = "Podany kod waluty nie jest z tabeli A";
    public static final String NAME_NOT_EXISTS = "Nazwa nie istnieje";
    public static final String SAME_CURRENCY_CODE = "Waluta PLN nie znajduje się w tabeli NBP";

    public static final String RATES_NOT_FOUND = "Dla podanego kodu waluty nie znaleziono stawek";
}
