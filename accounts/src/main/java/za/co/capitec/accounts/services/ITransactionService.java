package za.co.capitec.accounts.services;

import org.springframework.data.domain.Pageable;
import za.co.capitec.accounts.dtos.requests.TransactionDto;
import za.co.capitec.accounts.dtos.response.ResponseDto;
import za.co.capitec.accounts.dtos.response.TransactionResponse;
import za.co.capitec.accounts.entity.Accounts;
import za.co.capitec.accounts.enums.Categories;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ITransactionService {
    TransactionResponse findAllByIdNumber(int pageNo, int pageSize, String sortBy, String sortDir,
                                          String idNumber,
                                          LocalDate dateFrom,
                                          LocalDate dateTo);
    ResponseDto transact(TransactionDto transactionDto);
    ResponseDto creditAccount(Accounts account, BigDecimal amount, Categories transactionType, String reference);
    ResponseDto debitAccount(Accounts account, BigDecimal amount, Categories transactionType, String reference);
}
