package za.co.capitec.loans.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import za.co.capitec.loans.dtos.records.LoanTransactionRecord;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class LoanTransactionResponse {
    private List<LoanTransactionRecord> content;
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private boolean isLast;
}

