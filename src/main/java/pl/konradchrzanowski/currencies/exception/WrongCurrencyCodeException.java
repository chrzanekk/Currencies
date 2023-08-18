package pl.konradchrzanowski.currencies.exception;

public class WrongCurrencyCodeException extends RuntimeException {

    public WrongCurrencyCodeException(String message) {
        super(message);
    }
}
