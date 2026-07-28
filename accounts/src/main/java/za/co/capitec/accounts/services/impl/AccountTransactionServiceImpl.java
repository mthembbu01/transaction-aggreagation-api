package za.co.capitec.accounts.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import za.co.capitec.accounts.entity.Accounts;
import za.co.capitec.accounts.entity.AccountsTransactions;
import za.co.capitec.accounts.repositories.TransactionRepository;
import za.co.capitec.accounts.services.IAccountService;
import za.co.capitec.accounts.services.ITransactionService;
import za.co.capitec.accounts.utilities.AccountUtils;
import za.co.capitec.coreapi.dtos.ResponseDto;
import za.co.capitec.coreapi.dtos.accounts.requests.TransactionDto;
import za.co.capitec.coreapi.dtos.accounts.response.TransactionResponse;
import za.co.capitec.coreapi.enums.Categories;
import za.co.capitec.coreapi.enums.accounts.AccountType;
import za.co.capitec.coreapi.exceptions.InsufficientFundsException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountTransactionServiceImpl implements ITransactionService {
    private final TransactionRepository transactionRepository;
    private final IAccountService accountService;
    /**
     *
     * @param transactionDto
     * @return
     */
    @Override
    public ResponseDto transact(TransactionDto transactionDto) {
        //-- 1. deconstruct the TransactionDto into its components
        Long accNumber = transactionDto.getAccountNumber();
        Accounts accounts = accountService.findAccounts(accNumber);
        BigDecimal amount = transactionDto.getAmount();
        Categories category = transactionDto.getCategory();
        String reference = transactionDto.getReference();
        //-- check the transaction type and call the appropriate method
        if(category == Categories.CREDIT || category == Categories.DEPOSIT) {
            return creditAccount(accounts, amount, category, reference);
        } else {
           return debitAccount(accounts, amount, category, reference);
        }
    }
    /**
     *
     * @param account
     * @param amount
     * @param transactionType
     * @param reference
     * @return
     */

    private ResponseDto creditAccount(Accounts account, BigDecimal amount, Categories transactionType, String reference) {
        List<AccountsTransactions> transactions = new ArrayList<>();
        //-- 1. credit the account
        //-- add the original amount
        BigDecimal newAmount = account.getBalance().add(amount);
        //-- add the new amount to the account balance
        if(account.getAccountType() == AccountType.SAVINGS) {
            //-- calculate the interest
            BigDecimal interest = applyInterest(amount);
            //-- add the interest to our new balance
            newAmount = newAmount.add(interest);
            //-- Create a transaction for an interest
            AccountsTransactions interestTransaction = AccountsTransactions.create(account,"Credit",Boolean.TRUE,interest, Categories.INTEREST, reference);
            //-- add to the transactions List
            transactions.add(interestTransaction);
        }
        //-- set the balance`
        account.setBalance(newAmount);
        //-- 2. Save the new balance
        ResponseDto responseDto = accountService.saveAccount(account);
        //-- 3. Create a transaction for the credit
        AccountsTransactions transaction = AccountsTransactions.create(account, "Credit", Boolean.TRUE, amount, transactionType, reference);
        //-- 4. add to the transactions List
        transactions.add(transaction);
        //-- 5. save the transaction
        transactionRepository.saveAll(transactions);
        //-- 5. return the response
        return responseDto;
    }
    /**
     *
     * @param account
     * @param amount
     * @param transactionType
     * @param reference
     * @return
     */
    private ResponseDto debitAccount(Accounts account, BigDecimal amount, Categories transactionType, String reference) {
        //-- 1. check for sufficient funds
        if (account.getBalance().compareTo(amount) < 0)
            throw new InsufficientFundsException("Insufficient funds!");
        //-- 2. Subtract the amount from the total balance
        BigDecimal newAmount = account.getBalance().subtract(amount);
        //-- subtract the original amount
        account.setBalance(newAmount);
        //-- 3. Save the new balance
        ResponseDto responseDto = accountService.saveAccount(account);
        //-- 4. Create a transaction for the credit
        AccountsTransactions transaction = AccountsTransactions.create(account, "Debit", Boolean.FALSE, amount, transactionType, reference);
        //-- 5. save the
        transactionRepository.save(transaction);
        //-- 6. return the response
        return responseDto;
    }
    /**
     *
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @param idNumber
     * @param dateFrom
     * @param dateTo
     * @return
     */
    @Override
    public TransactionResponse findAllByIdNumber(int pageNo, int pageSize, String sortBy, String sortDir, String idNumber,
                                                 LocalDate dateFrom,
                                                 LocalDate dateTo) {
        //-- conditional sort object declaration
        Sort sort = sortDirection(sortBy, sortDir);
        //-- 1. create a pageable instance. Add sortDirection: Ascending or Descending order
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        //-- 2. Find all AccountsTransactions using the Pageable, idNumber, startDate, and endDate above
        Page<AccountsTransactions> page = transactionRepository.findByAccountsIdNumberAndDateBetween(pageable, idNumber, dateFrom, dateTo);
        //-- 3. Return the Customer Response for pagination
        return AccountUtils.toTransactionResponse(page);
    }
    /**
     *
     * @param amount
     * @return
     */
    private BigDecimal applyInterest(BigDecimal amount) {
        return amount.multiply(new BigDecimal("0.005")); // 0.5%
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
