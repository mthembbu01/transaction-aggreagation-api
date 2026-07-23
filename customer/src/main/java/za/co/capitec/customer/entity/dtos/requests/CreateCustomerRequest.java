package za.co.capitec.customer.entity.dtos.requests;

import jakarta.validation.constraints.Pattern;

public record CreateCustomerRequest(String firstname,
                                    String lastName,
                                    String address,
                                    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
                                    String email,
                                    @Pattern(regexp = "^(\\+27|0)?[1-9]\\d{8}$", message = "Invalid mobile number format")
                                    String mobileNumber,
                                    @Pattern(regexp = "^(\\d{13})$", message = "Invalid ID number format")
                                    String idNumber) {
}
