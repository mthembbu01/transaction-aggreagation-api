package za.co.capitec.accounts.dtos.requests;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransferDto {
    private Long fromAccount;
    private Long toAccount;
    private BigDecimal amount;
    private String reference;
}
