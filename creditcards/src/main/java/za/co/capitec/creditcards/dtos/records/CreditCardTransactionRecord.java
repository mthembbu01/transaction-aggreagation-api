package za.co.capitec.creditcards.dtos.records;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.capitec.creditcards.enums.Categories;
import za.co.capitec.creditcards.enums.CreditCardType;

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

