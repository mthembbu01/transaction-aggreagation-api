package za.co.capitec.coreapi.dtos.accounts.requests;

import lombok.Builder;
import lombok.Data;
import za.co.capitec.coreapi.enums.Categories;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionDto {
    private Long accountNumber;
    private String description;
    private BigDecimal amount;
    private Boolean isImmediate;
    private Categories category;
    private String reference;
}
