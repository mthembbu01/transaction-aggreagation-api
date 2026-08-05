package za.co.capitec.creditcards.dtos.requests;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.capitec.creditcards.enums.CreditCardType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCreditCardDto {
    private CreditCardType cardType;

    @Pattern(regexp = "^(\\+27|0)?[1-9]\\d{8}$", message = "Invalid mobile number")
    private String mobileNumber;

    @Pattern(regexp = "^\\d{13}$", message = "Invalid ID Number")
    private String idNumber;

    private BigDecimal creditLimit;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private boolean activeSw;
}

