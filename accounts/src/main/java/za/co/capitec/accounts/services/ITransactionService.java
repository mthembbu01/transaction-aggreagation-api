package za.co.capitec.accounts.services;

import za.co.capitec.accounts.entity.Accounts;
import za.co.capitec.coreapi.dtos.accounts.requests.TransactionDto;
import za.co.capitec.coreapi.dtos.ResponseDto;
import za.co.capitec.coreapi.dtos.accounts.response.TransactionResponse;
import za.co.capitec.coreapi.enums.Categories;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ITransactionService {
    TransactionResponse findAllByIdNumber(int pageNo, int pageSize, String sortBy, String sortDir,
                                          String idNumber,
                                          LocalDate dateFrom,
                                          LocalDate dateTo);
    ResponseDto transact(TransactionDto transactionDto);
}
