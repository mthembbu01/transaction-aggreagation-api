package za.co.capitec.accounts.dtos.requests;

import lombok.Builder;
import lombok.Data;
import za.co.capitec.accounts.enums.Categories;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransactionDto {
    private Long accountNumber;
    private String description;
    private Double amount;
    private Boolean isImmediate = Boolean.FALSE;
    private LocalDate timestamp;
    private Categories category;
    private String reference;
}
