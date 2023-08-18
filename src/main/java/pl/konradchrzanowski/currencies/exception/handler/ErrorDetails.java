package pl.konradchrzanowski.currencies.exception.handler;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class ErrorDetails {
    private final LocalDateTime timestamp;
    private final String message;
    private final String details;


}
