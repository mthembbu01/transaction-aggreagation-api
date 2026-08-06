package za.co.capitec.api_gateway.services.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;
import za.co.capitec.coreapi.dtos.accounts.response.TransactionResponse;
import za.co.capitec.coreapi.dtos.creditcards.response.CreditCardTransactionResponse;
import za.co.capitec.coreapi.dtos.customer.records.CustomersRecord;
import za.co.capitec.coreapi.dtos.loans.response.LoanTransactionResponse;

public interface CustomerTransactionsSummaryClient {

    @GetExchange(value = "/capitec/customers/api/v1/{idNumber}",accept = "application/json")
    Mono<ResponseEntity<CustomersRecord>> fetchCustomerDetails(@PathVariable(value = "idNumber") String idNumber);

    @GetExchange(value = "/capitec/accounts/api/v1/transaction/{idNumber}/{startDate}/{endDate}?pageNo=0&pageSize=10&sortBy=date&sortDir=asc",accept = "application/json")
    Mono<ResponseEntity<TransactionResponse>> fetchAccountTransactions(@PathVariable(value = "idNumber") String idNumber,
                                                                       @PathVariable(value = "startDate") String startDate,
                                                                       @PathVariable(value = "endDate") String endDate);

    @GetExchange(value = "/capitec/cards/api/v1/transaction/{idNumber}/{startDate}/{endDate}?pageNo=0&pageSize=10&sortBy=date&sortDir=asc",accept = "application/json")
    Mono<ResponseEntity<CreditCardTransactionResponse>> fetchCreditCardTransactions(@PathVariable(value = "idNumber") String idNumber,
                                                                                    @PathVariable(value = "startDate") String startDate,
                                                                                    @PathVariable(value = "endDate") String endDate);

    @GetExchange(value = "/capitec/loans/api/v1/transaction/{idNumber}/{startDate}/{endDate}?pageNo=0&pageSize=10&sortBy=date&sortDir=asc",accept = "application/json")
    Mono<ResponseEntity<LoanTransactionResponse>> fetchLoanTransactions(@PathVariable(value = "idNumber") String idNumber,
                                                                       @PathVariable(value = "startDate") String startDate,
                                                                       @PathVariable(value = "endDate") String endDate);

}
