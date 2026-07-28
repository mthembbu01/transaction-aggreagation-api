package za.co.capitec.api_gateway.dto.accountsTransactions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class AccTransactionResponse {
    private List<AccTransactionRecord> content;
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private boolean isLast;
}
