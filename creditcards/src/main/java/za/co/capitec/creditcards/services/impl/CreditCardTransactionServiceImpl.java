package za.co.capitec.creditcards.services.impl;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import za.co.capitec.creditcards.dtos.requests.CreditCardTransactionDto;
import za.co.capitec.creditcards.dtos.response.CreditCardTransactionResponse;
import za.co.capitec.creditcards.dtos.response.ResponseDto;
import za.co.capitec.creditcards.entity.CreditCardTransactions;
import za.co.capitec.creditcards.entity.CreditCards;
import za.co.capitec.creditcards.enums.Categories;
import za.co.capitec.creditcards.exceptions.InsufficientFundsException;
import za.co.capitec.creditcards.repositories.CreditCardTransactionRepository;
import za.co.capitec.creditcards.services.ICCTransactionService;
import za.co.capitec.creditcards.services.ICreditCardService;
import za.co.capitec.creditcards.utilities.CreditCardUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CreditCardTransactionServiceImpl implements ICCTransactionService {

    private final CreditCardTransactionRepository transactionRepository;

    private final ICreditCardService creditCardService;
    /**
     *
     * @param transactionDto
     * @return
     */
    @Override
    public ResponseDto transact(CreditCardTransactionDto transactionDto) {
        //-- 1. deconstruct the tTRansactionDto into the Credit Card Components
        Long cardNumber = transactionDto.getCardNumber();
        CreditCards creditCard = creditCardService.findCreditCard(cardNumber);
        String description = transactionDto.getDescription();
        BigDecimal amount = transactionDto.getAmount();
        Categories category = transactionDto.getCategory();
        //-- 2. Perform credit card operation based on the category
        if(category == Categories.CREDIT || category == Categories.DEPOSIT){
            return creditAccount(creditCard, amount, category, description);
        } else {
            return debitAccount(creditCard, amount, category, description);
        }
    }
    /**
     *
     * @param creditCard
     * @param amount
     * @param categories
     * @param description
     * @return
     */
    private ResponseDto creditAccount(CreditCards creditCard, BigDecimal amount, Categories categories, String description) {
        //-- 1. Credit the account
        //-- add the to the original amount
        BigDecimal totalAmount = creditCard.getAmount().add(amount);
        //-- 2. set the credit card balance
        creditCard.setAmount(totalAmount);
        //-- 3. save the new balance amount
        ResponseDto responseDto = creditCardService.saveCreditCard(creditCard);
        //-- 4. Create a credit card transaction
        CreditCardTransactions transaction = CreditCardTransactions.create(creditCard, description, amount, categories);
        transactionRepository.save(transaction);
        //-- 5. return a proper message
        return responseDto;
    }
    /**
     *
     * @param creditCard
     * @param amount
     * @param transactionType
     * @param description
     * @return
     */
    private ResponseDto debitAccount(CreditCards creditCard, BigDecimal amount, Categories transactionType, String description) {
        //-- 1. check for sufficient funds
        if (creditCard.getAmount().compareTo(amount) < 0)
            throw new InsufficientFundsException("Insufficient funds!");
        //-- 2. Subtract the amount from the total balance
        BigDecimal newAmount = creditCard.getAmount().subtract(amount);
        //-- subtract the original amount
        creditCard.setAmount(newAmount);
        //-- 3. Save the new balance
        ResponseDto responseDto = creditCardService.saveCreditCard(creditCard);
        //-- 4. Create a transaction for the credit
        CreditCardTransactions transaction = CreditCardTransactions.create(creditCard, description, amount, transactionType);
        //-- 5. save the
        transactionRepository.save(transaction);
        //-- 6. return the response
        return responseDto;
    }

    @Override
    public CreditCardTransactionResponse findAllByIdNumber(int pageNo, int pageSize, String sortBy, String sortDir, String idNumber,
                                                           LocalDate dateFrom,
                                                           LocalDate dateTo) {
        //-- conditional sort object declaration
        Sort sort = sortDirection(sortBy, sortDir);
        //-- 1. create a pageable instance. Add sortDirection: Ascending or Descending order
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        //-- 2. Find all AccountsTransactions using the Pageable, idNumber, startDate, and endDate above
        Page<CreditCardTransactions> transactionsPage = transactionRepository.findByCreditCardIdNumberAndDateBetween(idNumber, pageable, dateFrom, dateTo);
        //-- 3. Return the CreditCard Response for Pagination
        return CreditCardUtils.toTransactionResponse(transactionsPage);
    }
    /**
     * @param sortBy ascending or descending order
     * @param sortDir
     * @return
     */
    private Sort sortDirection(String sortBy, String sortDir) {
        return sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
    }
}
