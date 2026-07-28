package za.co.capitec.coreapi.dtos.customer.requests;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {
    private String firstname;
    private String lastName;
    private String address;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^(\\+27|0)?[1-9]\\d{8}$", message = "Invalid mobile number format")
    private String mobileNumber;

    @Pattern(regexp = "^(\\d{13})$", message = "Invalid ID number format")
    private String idNumber;
}
