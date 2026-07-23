package za.co.capitec.creditcards.entity;

import lombok.*;
import za.co.capitec.creditcards.enums.CreditCardType;

//@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreditCards extends BaseEntity {
    private Long id;
    private Long cardNumber;
    private CreditCardType cardType; // VISA, MASTERCARD, AMEX
    private String mobileNumber;
    private String idNumber;
    private Double creditLimit;
    private Double availableCredit;
    private boolean activeSw;
}

