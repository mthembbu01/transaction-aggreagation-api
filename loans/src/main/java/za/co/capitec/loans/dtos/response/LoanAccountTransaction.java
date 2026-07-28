package za.co.capitec.loans.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

