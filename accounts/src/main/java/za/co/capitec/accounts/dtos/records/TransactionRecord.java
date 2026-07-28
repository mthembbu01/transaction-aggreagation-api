package za.co.capitec.accounts.dtos.records;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.capitec.accounts.enums.AccountType;
import za.co.capitec.accounts.enums.Categories;

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
