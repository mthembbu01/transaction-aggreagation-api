package za.co.capitec.customer.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiErrorResponse(
        HttpStatus status,
        String message,
        String path,
        String handlerMethod,
        ZonedDateTime timestamp
) {}
