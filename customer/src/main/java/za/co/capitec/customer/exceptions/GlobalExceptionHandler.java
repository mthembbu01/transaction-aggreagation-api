package za.co.capitec.customer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ServerWebExchange;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ApiErrorResponse handleCustomerAlreadyExistsException(
            ResourceAlreadyExistsException ex,
            ServerWebExchange exchange,
            HandlerMethod method) {

        return new ApiErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                exchange.getRequest().getURI().getPath(),
                method.getMethod().getName(),
                ZonedDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiErrorResponse handleCustomerNotFoundException(
            ResourceNotFoundException ex,
            ServerWebExchange exchange,
            HandlerMethod method) {

        return new ApiErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                exchange.getRequest().getURI().getPath(),
                method.getMethod().getName(),
                ZonedDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiErrorResponse handleIllegalArgumentException(
            IllegalArgumentException ex,
            ServerWebExchange exchange,
            HandlerMethod method) {

        return new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                exchange.getRequest().getURI().getPath(),
                method.getMethod().getName(),
                ZonedDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiErrorResponse handleGenericException(
            Exception ex,
            ServerWebExchange exchange,
            HandlerMethod method) {

        return new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                exchange.getRequest().getURI().getPath(),
                method.getMethod().getName(),
                ZonedDateTime.now()
        );
    }
}