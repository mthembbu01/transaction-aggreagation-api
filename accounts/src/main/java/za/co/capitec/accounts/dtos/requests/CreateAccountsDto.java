package za.co.capitec.accounts.dtos.requests;

import jakarta.validation.constraints.Pattern;

public record CreateAccountsDto(String accountType,
                                @Pattern(regexp = "^(\\+27|0)?[1-9]\\d{8}$", message = "Invalid mobile number")
                                String mobileNumber,
                                @Pattern(regexp = "^\\d{13}$", message = "Invalid ID Number")
                                String idNumber,
                                String branchAddress){}