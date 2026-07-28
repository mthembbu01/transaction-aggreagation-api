package za.co.capitec.accounts.utils;

import org.springframework.data.domain.Page;
import za.co.capitec.accounts.entity.AccountsTransactions;
import za.co.capitec.accounts.utilities.AccountUtils;
import za.co.capitec.coreapi.dtos.accounts.records.TransactionRecord;
import za.co.capitec.coreapi.dtos.accounts.response.TransactionResponse;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AccountsUtilities {

    public static Long setAccountNumber(){return 1234567890L;}
    public static Long updatedAccountNumber(){ return 1234567891L;}

    public static String setContactNumber(){return "0788298725";}
    public static String updatedContactNumber(){return "0788298726";}

    public static String setIdNumber(){return "9202204720082";}
    public static String updatedIdNumber(){return "9202204720083";}

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
