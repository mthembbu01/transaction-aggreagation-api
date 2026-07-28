package za.co.capitec.coreapi.dtos.accounts.response;

import lombok.*;
import za.co.capitec.coreapi.dtos.accounts.response.TransactionResponse;
import za.co.capitec.coreapi.enums.accounts.AccountType;

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
