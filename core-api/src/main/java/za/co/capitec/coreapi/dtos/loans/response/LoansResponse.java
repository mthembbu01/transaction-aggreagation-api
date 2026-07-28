package za.co.capitec.coreapi.dtos.loans.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import za.co.capitec.coreapi.dtos.loans.records.LoanRecord;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class LoansResponse {
    private List<LoanRecord> content;
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private boolean isLast;
}

