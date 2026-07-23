package za.co.capitec.api_gateway.dto.accountsTransactions;

import lombok.Builder;
import za.co.capitec.api_gateway.enums.AccountType;
import za.co.capitec.api_gateway.enums.Categories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record TransactionRecord(AccountType accountType,
                                BigDecimal amount,
                                Categories category,
                                String reference,
                                LocalTime time,
                                LocalDate date) { }
