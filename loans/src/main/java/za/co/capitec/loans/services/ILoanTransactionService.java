package za.co.capitec.loans.services;





import za.co.capitec.loans.dtos.records.LoanTransactionRecord;
import za.co.capitec.loans.dtos.requests.LoanTransactionDto;
import za.co.capitec.loans.dtos.response.LoanTransactionResponse;
import za.co.capitec.loans.dtos.response.ResponseDto;
import za.co.capitec.loans.entity.Loans;
import za.co.capitec.loans.enums.Categories;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ILoanTransactionService {
    LoanTransactionResponse findAllByIdNumber(int pageNo, int pageSize, String sortBy, String sortDir,
                                              String idNumber,
                                              LocalDate dateFrom,
                                              LocalDate dateTo);
    ResponseDto transact(LoanTransactionDto transactionDto);
    ResponseDto creditAccount(Loans loan, BigDecimal amount, Categories transactionType, String reference);
    ResponseDto debitAccount(Loans loan, BigDecimal amount, Categories transactionType, String reference);
}
