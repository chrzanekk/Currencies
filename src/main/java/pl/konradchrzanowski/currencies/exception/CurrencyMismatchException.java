package pl.konradchrzanowski.currencies.exception;

public class CurrencyMismatchException extends RuntimeException{

    public CurrencyMismatchException(String message) {
        super(message);
    }
}
