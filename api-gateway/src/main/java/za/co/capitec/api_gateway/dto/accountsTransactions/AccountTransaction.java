package za.co.capitec.api_gateway.dto.accountsTransactions;

import lombok.Builder;
import za.co.capitec.api_gateway.enums.AccountType;

import java.math.BigDecimal;

@Builder
public record AccountTransaction(
        Long accountNumber,
        AccountType accountType,
        BigDecimal currentBalance,
        TransactionResponse transactions

) {}