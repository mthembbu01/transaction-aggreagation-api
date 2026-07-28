package za.co.capitec.loans.dtos.requests;

import lombok.Builder;
import lombok.Data;
import za.co.capitec.loans.enums.Categories;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class LoanTransactionDto {
    private Long loanNumber;
    private String description;
    private BigDecimal amount;
    private Boolean isImmediate = Boolean.FALSE;
    private LocalDate timestamp;
    private Categories category;
    private String reference;
}

