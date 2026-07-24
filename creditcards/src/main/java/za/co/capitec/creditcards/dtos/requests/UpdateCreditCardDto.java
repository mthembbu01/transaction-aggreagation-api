package za.co.capitec.creditcards.dtos.requests;

import jakarta.validation.constraints.Pattern;
import za.co.capitec.creditcards.enums.CreditCardType;

import java.time.LocalDate;

public record UpdateCreditCardDto(@Pattern(regexp = "^(\\+27|0)?[1-9]\\d{8}$", message = "Invalid mobile number") String mobileNumber,
                                  @Pattern(regexp = "^\\d{13}$", message = "Invalid ID Number") String idNumber,
                                  Double creditLimit,
                                  LocalDate expiryDate,
                                  boolean activeSw) {
}

