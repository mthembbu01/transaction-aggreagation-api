package za.co.capitec.creditcards.dtos.requests;

import lombok.Builder;
import lombok.Data;
import za.co.capitec.creditcards.enums.Categories;

import java.time.LocalDate;

@Data
@Builder
public class CreditCardTransactionDto {
    private Long cardNumber;
    private String description;
    private Double amount;
    private Boolean isImmediate = Boolean.FALSE;
    private LocalDate timestamp;
    private Categories category;
    private String reference;
}

