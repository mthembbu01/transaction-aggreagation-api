package za.co.capitec.accounts.utilities;

import org.springframework.data.domain.Page;
import za.co.capitec.accounts.dtos.records.TransactionRecord;
import za.co.capitec.accounts.dtos.response.TransactionResponse;
import za.co.capitec.accounts.entity.Transactions;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AccountUtils {
    /**
     *
     * @return
     */
    public static Long generateAccNumber() {
        //--
        return ThreadLocalRandom.current()
                .nextLong(1_000_000_000L, 10_000_000_000L);
    }
    /**
     *
     * @param transactions
     * @return
     */
    public static TransactionRecord toTransactionRecord(Transactions transactions){
        return new TransactionRecord(transactions.getAccountType(),
                transactions.getAmount(),
                transactions.getCategory(),
                transactions.getReference(),
                transactions.getTime(),
                transactions.getDate());
    }
    /**
     *
     * @param pages
     * @return
     */
    public static TransactionResponse toTransactionResponse(Page<Transactions> pages){
        //-- 1. Define a list of Transaction Records
        List<TransactionRecord> content = pages.getContent()
                .stream()
                .map(AccountUtils::toTransactionRecord)
                .toList();
        //-- 2. Return the Transaction Response for pagination
        return TransactionResponse.builder()
                .content(content)
                .pageNo(pages.getNumber())
                .pageSize(pages.getSize())
                .totalPages(pages.getTotalPages())
                .totalElements(pages.getTotalElements())
                .isLast(pages.isLast())
                .build();
    }
}
