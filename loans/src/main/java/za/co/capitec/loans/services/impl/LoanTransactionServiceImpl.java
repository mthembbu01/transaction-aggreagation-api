package za.co.capitec.loans.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import za.co.capitec.coreapi.dtos.ResponseDto;
import za.co.capitec.coreapi.dtos.loans.requests.LoanTransactionDto;
import za.co.capitec.coreapi.dtos.loans.response.LoanTransactionResponse;
import za.co.capitec.coreapi.enums.Categories;
import za.co.capitec.loans.entity.LoanTransactions;
import za.co.capitec.loans.entity.Loans;
import za.co.capitec.loans.exceptions.InsufficientFundsException;
import za.co.capitec.loans.repositories.LoanTransactionRepository;
import za.co.capitec.loans.services.ILoanService;
import za.co.capitec.loans.services.ILoanTransactionService;
import za.co.capitec.loans.utilities.LoanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class LoanTransactionServiceImpl implements ILoanTransactionService {
    private final LoanTransactionRepository transactionRepository;
    private final ILoanService loanService;

    @Override
    public ResponseDto transact(LoanTransactionDto transactionDto) {
        //-- 1. Deconstruct the component into it's basic components
        Long loanNumber = transactionDto.getLoanNumber();
        Loans loan = loanService.findLoan(loanNumber);
        BigDecimal amount = transactionDto.getAmount();
        Categories categories = transactionDto.getCategory();
        String reference = transactionDto.getReference();
        //-- check the transaction type and call the correct method
        if(categories == Categories.CREDIT || categories == Categories.DEPOSIT)
            return creditAccount(loan, amount, categories, reference);
         else
            return debitAccount(loan, amount, categories, reference);
    }
    /**
     *
     * @param loan
     * @param amount
     * @param categories
     * @param reference
     * @return
     */
    @Override
    public ResponseDto creditAccount(Loans loan, BigDecimal amount, Categories categories, String reference) {
        //-- 1. Credit the account
        //-- add the to the original amount
        BigDecimal totalAmount = loan.getLoanAmount().add(amount);
        //-- 2. set the loan balance
        loan.setLoanAmount(totalAmount);
        loan.setOutstandingAmount(loan.getOutstandingAmount().subtract(amount));
        loan.setOutstandingBalance(loan.getOutstandingAmount().subtract(amount));
        //-- 3. save the new balance amount
        ResponseDto responseDto = loanService.saveLoan(loan);
        //-- 4. Create a credit card transaction
        LoanTransactions transaction = LoanTransactions.create(loan, amount, categories, reference);
        transactionRepository.save(transaction);
        //-- 5. return a proper message
        return responseDto;
    }
    /**
     *
     * @param loan
     * @param amount
     * @param categories
     * @param reference
     * @return
     */
    @Override
    public ResponseDto debitAccount(Loans loan, BigDecimal amount, Categories categories, String reference) {
        //-- 1. check for sufficient funds
        if (loan.getLoanAmount().compareTo(amount) < 0)
            throw new InsufficientFundsException("Insufficient funds!");
        //-- 2. Subtract the amount from the total balance
        BigDecimal newAmount = loan.getLoanAmount().subtract(amount);
        //-- subtract the original amount
        loan.setLoanAmount(newAmount);
        //-- 3. Save the new balance
        ResponseDto responseDto = loanService.saveLoan(loan);
        //-- 4. Create a transaction for the credit
        LoanTransactions transaction = LoanTransactions.create(loan, amount, categories, reference);
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
    public LoanTransactionResponse findAllByIdNumber(int pageNo, int pageSize, String sortBy, String sortDir, String idNumber,
                                                     LocalDate dateFrom,
                                                     LocalDate dateTo) {
        //-- conditional sort object declaration
        Sort sort = sortDirection(sortBy, sortDir);
        //-- 1. create a pageable instance. Add sortDirection: Ascending or Descending order
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        //-- 2. Find all LoanTransactions using the Pageable, idNumber, startDate, and endDate above
        Page<LoanTransactions> transactionsPage = transactionRepository.findByLoanIdNumberAndDateBetween(idNumber, pageable, dateFrom, dateTo);
        //-- 3. Return the Loan Transaction Response for Pagination
        return LoanUtils.toTransactionResponse(transactionsPage);
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
