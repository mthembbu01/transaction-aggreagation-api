package za.co.capitec.api_gateway.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDto {

    @NotEmpty(message = "Name cannot be null or empty")
    @Size(min = 5, max = 30, message = "The length of the name should be between 5 and 30")
    private String name;

    @NotEmpty(message = "Surname cannot be null or empty")
    @Size(min = 5, max = 30, message = "The length of the surname should be between 5 and 30")
    private String surname;

    @NotEmpty(message = "Email address cannot be null or empty")
    @Email(message = "Email address should be a valid value")
    private String email;

    @NotEmpty(message = "ID number cannot be null or empty")
    @Pattern(regexp = "[0-9]{13}", message = "ID number must be 13 digits")
    private String idNumber;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    private Boolean activeSw;
}
