package za.co.capitec.loans.utilities;

import org.springframework.data.domain.Page;
import za.co.capitec.coreapi.dtos.loans.records.LoanTransactionRecord;
import za.co.capitec.coreapi.dtos.loans.response.LoanTransactionResponse;
import za.co.capitec.loans.entity.LoanTransactions;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LoanUtils {

    public static String generateLoanNumber() {
        long number = ThreadLocalRandom.current()
                .nextLong(1_000_000_000L, 10_000_000_000L);
        return String.valueOf(number);
    }

    /**
     * The method
     * @param loanTransactions
     * @return
     */
    public static LoanTransactionRecord toTransactionRecord(LoanTransactions loanTransactions){
        return new LoanTransactionRecord(loanTransactions.getLoan().getLoanNumber(),
                loanTransactions.getAmount(),
                loanTransactions.getCategory(),
                loanTransactions.getReference(),
                loanTransactions.getTime(),
                loanTransactions.getDate());
    }
    /**
     * The method to convert a Page of LoanTransactions into a LoanTransactionResponse for pagination.
     * @param pages
     * @return
     */
    public static LoanTransactionResponse toTransactionResponse(Page<LoanTransactions> pages) {
        //-- 1. Define a list of Transaction Records for Credit Card Transactions
        List<LoanTransactionRecord> content = pages.getContent()
                .stream()
                .map(LoanUtils::toTransactionRecord)
                .toList();
        //-- 2. Return the Loan Transactions Response for Pagination
        return LoanTransactionResponse.builder()
                .content(content)
                .pageNo(pages.getNumber())
                .pageSize(pages.getSize())
                .totalPages(pages.getTotalPages())
                .totalElements(pages.getTotalElements())
                .isLast(pages.isLast())
                .build();
    }
}

