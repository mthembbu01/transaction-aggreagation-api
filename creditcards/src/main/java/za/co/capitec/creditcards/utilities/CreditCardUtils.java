package za.co.capitec.creditcards.utilities;

import org.springframework.data.domain.Page;
import za.co.capitec.creditcards.dtos.records.CreditCardTransactionRecord;
import za.co.capitec.creditcards.dtos.response.CreditCardTransactionResponse;
import za.co.capitec.creditcards.entity.CreditCardTransactions;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CreditCardUtils {
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
     * @return
     */
    public static Long generateCardNumber() {
        return ThreadLocalRandom.current()
                .nextLong(1_000_000_000_000_0000L, 9_999_999_999_999_9999L);
    }
    /**
     * The method to convert a CreditCardTransactions entity into a CreditCardTransactionRecord.
     * @param creditCardTransactions
     * @return
     */
    public static CreditCardTransactionRecord toTransactionRecord(CreditCardTransactions creditCardTransactions){
        return new CreditCardTransactionRecord(creditCardTransactions.getCardType(),
                creditCardTransactions.getCategory(),
                creditCardTransactions.getAmount(),
                creditCardTransactions.getTime(),
                creditCardTransactions.getDate());
    }
    /**
     * The method to convert a Page of CreditCardTransactions into a CreditCardTransactionResponse for pagination.
     * @param pages
     * @return
     */
    public static CreditCardTransactionResponse toTransactionResponse(Page<CreditCardTransactions> pages) {
        //-- 1. Define a list of Transaction Records for Credit Card Transactions
        List<CreditCardTransactionRecord> content = pages.getContent()
                .stream()
                .map(CreditCardUtils::toTransactionRecord)
                .toList();
        //-- 2. Return the Loan Transactions Response for Pagination
        return CreditCardTransactionResponse.builder()
                .content(content)
                .pageNo(pages.getNumber())
                .pageSize(pages.getSize())
                .totalPages(pages.getTotalPages())
                .totalElements(pages.getTotalElements())
                .isLast(pages.isLast())
                .build();
    }
}

