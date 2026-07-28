package za.co.capitec.creditcards.services;



import za.co.capitec.creditcards.dtos.requests.CreditCardTransactionDto;
import za.co.capitec.creditcards.dtos.response.CreditCardTransactionResponse;
import za.co.capitec.creditcards.dtos.response.ResponseDto;

import java.time.LocalDate;

public interface ICCTransactionService {
    CreditCardTransactionResponse findAllByIdNumber(int pageNo, int pageSize, String sortBy, String sortDir,
                                                    String idNumber,
                                                    LocalDate dateFrom,
                                                    LocalDate dateTo);
    ResponseDto transact(CreditCardTransactionDto transactionDto);
}
