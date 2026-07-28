package za.co.capitec.coreapi.dtos.creditcards.records;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.capitec.coreapi.enums.creditcards.CreditCardType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardRecord {
    private Long cardNumber;
    private CreditCardType cardType;
    private String mobileNumber;
    private String idNumber;
    private Double creditLimit;
    private Double availableCredit;
    private Double outstandingBalance;
    private Double minimumPayment;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private boolean activeSw;
}

