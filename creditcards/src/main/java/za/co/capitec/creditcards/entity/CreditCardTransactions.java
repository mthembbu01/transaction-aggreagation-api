package za.co.capitec.creditcards.entity;

import lombok.*;
import za.co.capitec.creditcards.enums.Categories;
import za.co.capitec.creditcards.enums.CreditCardType;

import java.time.LocalDate;
import java.time.LocalTime;

//@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardTransactions {
    private Long id;
    private Long cardNumber;
    private CreditCardType cardType;
    private String description;
    private Double amount;
    private Boolean isImmediate = Boolean.FALSE;
    private LocalTime time;
    private LocalDate date;
    private Categories category;
    private String reference;
}

