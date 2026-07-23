package za.co.capitec.creditcards.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import za.co.capitec.creditcards.dtos.records.CreditCardTransactionRecord;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CreditCardTransactionResponse {
    private List<CreditCardTransactionRecord> content;
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private boolean isLast;
}

