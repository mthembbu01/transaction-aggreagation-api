package za.co.capitec.api_gateway.dto.accountsTransactions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.capitec.api_gateway.enums.AccountType;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountTransaction {
    private Long accountNumber;
    private AccountType accountType;
    private BigDecimal currentBalance;
    private AccTransactionResponse transactions;
}
