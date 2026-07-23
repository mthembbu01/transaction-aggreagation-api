package za.co.capitec.creditcards.dtos.records;

import lombok.Builder;
import za.co.capitec.creditcards.enums.Categories;
import za.co.capitec.creditcards.enums.CreditCardType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record CreditCardTransactionRecord(CreditCardType cardType,
                                           BigDecimal amount,
                                           Categories category,
                                           String reference,
                                           LocalTime time,
                                           LocalDate date) {
}

