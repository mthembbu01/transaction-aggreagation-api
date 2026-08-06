package za.co.capitec.loans.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ApiErrorResponse handleResourceAlreadyExistsException(
            ResourceAlreadyExistsException ex,
            WebRequest webRequest) {

        return new ApiErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                getRequestUri(webRequest),
                "N/A",
                ZonedDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiErrorResponse handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest webRequest) {

        return new ApiErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                getRequestUri(webRequest),
                "N/A",
                ZonedDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiErrorResponse handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest webRequest) {

        return new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                getRequestUri(webRequest),
                "N/A",
                ZonedDateTime.now()
        );
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InsufficientFundsException.class)
    public ApiErrorResponse handleInsufficientFundsException(
            InsufficientFundsException ex,
            WebRequest webRequest) {

        return new ApiErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getMessage(),
                getRequestUri(webRequest),
                "N/A",
                ZonedDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiErrorResponse handleGenericException(
            Exception ex,
            WebRequest webRequest) {

        return new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                getRequestUri(webRequest),
                "N/A",
                ZonedDateTime.now()
        );
    }

    private String getRequestUri(WebRequest webRequest) {
        return webRequest.getDescription(false).replace("uri=", "");
    }
}

