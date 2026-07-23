package za.co.capitec.loans.dtos.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record LoanAccountTransaction(
        Long loanNumber,
        BigDecimal outstandingBalance,
        LoanTransactionResponse transactions
) {
}

