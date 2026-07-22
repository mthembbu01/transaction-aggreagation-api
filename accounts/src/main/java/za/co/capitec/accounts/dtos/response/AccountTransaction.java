package za.co.capitec.accounts.dtos.response;

import lombok.Builder;
import za.co.capitec.accounts.enums.AccountType;

import java.math.BigDecimal;

@Builder
public record AccountTransaction(
        Long accountNumber,
        AccountType accountType,
        BigDecimal currentBalance,
        TransactionResponse transactions

) {}