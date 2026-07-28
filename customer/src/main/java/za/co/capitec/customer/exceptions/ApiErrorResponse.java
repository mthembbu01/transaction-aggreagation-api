package za.co.capitec.customer.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    private HttpStatus status;
    private String message;
    private String path;
    private String handlerMethod;
    private ZonedDateTime timestamp;
}
