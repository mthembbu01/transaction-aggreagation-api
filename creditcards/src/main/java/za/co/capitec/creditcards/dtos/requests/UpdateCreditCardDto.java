package za.co.capitec.creditcards.dtos.requests;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCreditCardDto {
    @Pattern(regexp = "^(\\+27|0)?[1-9]\\d{8}$", message = "Invalid mobile number")
    private String mobileNumber;

    @Pattern(regexp = "^\\d{13}$", message = "Invalid ID Number")
    private String idNumber;

    private BigDecimal creditLimit;
    private LocalDate expiryDate;
    private boolean activeSw;
}

