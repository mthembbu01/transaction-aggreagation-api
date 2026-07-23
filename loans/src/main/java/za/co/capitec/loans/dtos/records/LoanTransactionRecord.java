package za.co.capitec.loans.dtos.records;

import lombok.Builder;
import za.co.capitec.loans.enums.Categories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record LoanTransactionRecord(
        BigDecimal amount,
        Categories category,
        String reference,
        LocalTime time,
        LocalDate date
) {
}

