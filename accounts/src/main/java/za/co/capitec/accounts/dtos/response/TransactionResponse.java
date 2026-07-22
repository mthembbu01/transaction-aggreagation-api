package za.co.capitec.accounts.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import za.co.capitec.accounts.dtos.records.TransactionRecord;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private List<TransactionRecord> content;
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private boolean isLast;
}
