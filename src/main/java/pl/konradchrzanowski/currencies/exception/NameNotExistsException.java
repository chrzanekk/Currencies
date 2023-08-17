package pl.konradchrzanowski.currencies.exception;

public class NameNotExistsException extends RuntimeException{
    public NameNotExistsException(String message) {
        super(message);
    }
}
