package za.co.capitec.accounts.utilities;

import org.springframework.data.domain.Page;
import za.co.capitec.accounts.entity.AccountsTransactions;
import za.co.capitec.coreapi.dtos.accounts.records.TransactionRecord;
import za.co.capitec.coreapi.dtos.accounts.response.TransactionResponse;

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
     * @param accountsTransactions
     * @return
     */
    public static TransactionRecord toTransactionRecord(AccountsTransactions accountsTransactions){
        return new TransactionRecord(accountsTransactions.getAccountType(),
                accountsTransactions.getAmount(),
                accountsTransactions.getCategory(),
                accountsTransactions.getReference(),
                accountsTransactions.getTime(),
                accountsTransactions.getDate());
    }
    /**
     *
     * @param pages
     * @return
     */
    public static TransactionResponse toTransactionResponse(Page<AccountsTransactions> pages){
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
