package za.co.capitec.creditcards.dtos.requests;

import jakarta.validation.constraints.Pattern;

public record CreateCreditCardDto(String cardType,
                                  @Pattern(regexp = "^(\\+27|0)?[1-9]\\d{8}$", message = "Invalid mobile number")
                                  String mobileNumber,
                                  @Pattern(regexp = "^\\d{13}$", message = "Invalid ID Number")
                                  String idNumber,
                                  Double creditLimit,
                                  boolean activeSw) {
}

