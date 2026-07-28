package za.co.capitec.coreapi.dtos.accounts.requests;


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
