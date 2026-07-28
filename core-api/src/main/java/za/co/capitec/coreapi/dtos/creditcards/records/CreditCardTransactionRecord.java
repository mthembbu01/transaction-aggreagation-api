package za.co.capitec.coreapi.dtos.creditcards.records;

import lombok.*;
import za.co.capitec.coreapi.enums.Categories;
import za.co.capitec.coreapi.enums.creditcards.CreditCardType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardTransactionRecord {
    private CreditCardType cardType;
    private Categories category;
    private BigDecimal amount;
    private LocalTime time;
    private LocalDate date;
}

