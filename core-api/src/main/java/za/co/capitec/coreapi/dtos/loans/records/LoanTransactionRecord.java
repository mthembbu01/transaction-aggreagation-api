package za.co.capitec.coreapi.dtos.loans.records;

import lombok.*;
import za.co.capitec.coreapi.enums.Categories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanTransactionRecord {
    private Long loanNumber;
    private BigDecimal amount;
    private Categories category;
    private String reference;
    private LocalTime time;
    private LocalDate date;
}

