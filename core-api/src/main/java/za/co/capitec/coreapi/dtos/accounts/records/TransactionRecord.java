package za.co.capitec.coreapi.dtos.accounts.records;

import lombok.*;
import za.co.capitec.coreapi.enums.accounts.AccountType;
import za.co.capitec.coreapi.enums.Categories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRecord {
    private AccountType accountType;
    private BigDecimal amount;
    private Categories category;
    private String reference;
    private LocalTime time;
    private LocalDate date;
}
