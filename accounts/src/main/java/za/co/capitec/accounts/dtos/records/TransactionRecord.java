package za.co.capitec.accounts.dtos.records;

import lombok.Builder;
import za.co.capitec.accounts.enums.AccountType;
import za.co.capitec.accounts.enums.Categories;

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
