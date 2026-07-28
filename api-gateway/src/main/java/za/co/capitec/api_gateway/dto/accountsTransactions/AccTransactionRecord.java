package za.co.capitec.api_gateway.dto.accountsTransactions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.capitec.api_gateway.enums.AccountType;
import za.co.capitec.api_gateway.enums.Categories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccTransactionRecord {
    private AccountType accountType;
    private BigDecimal amount;
    private Categories category;
    private String reference;
    private LocalTime time;
    private LocalDate date;
}


