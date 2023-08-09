package pl.konradchrzanowski.currencies.exception;

public class RatesNotFoundException extends RuntimeException{
    public RatesNotFoundException(String message) {
        super(message);
    }
}
