package za.co.capitec.coreapi.dtos.loans.response;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanAccountTransaction {
    private Long loanNumber;
    private BigDecimal outstandingBalance;
    private LoanTransactionResponse transactions;
}

