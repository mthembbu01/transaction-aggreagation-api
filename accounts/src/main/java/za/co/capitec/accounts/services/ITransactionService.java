package za.co.capitec.accounts.services;

import org.springframework.data.domain.Pageable;
import za.co.capitec.accounts.dtos.response.TransactionResponse;

import java.time.LocalDate;

public interface ITransactionService {
    TransactionResponse findAllByAccountNumber(Long accountNumber, Pageable pageable, LocalDate dateFrom, LocalDate dateTo);
    TransactionResponse findAllByIdNumber(String idNumber, Pageable pageable, LocalDate dateFrom, LocalDate dateTo);
}
