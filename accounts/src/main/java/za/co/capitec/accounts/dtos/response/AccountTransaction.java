package za.co.capitec.accounts.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.capitec.accounts.enums.AccountType;

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
    private TransactionResponse transactions;
}
