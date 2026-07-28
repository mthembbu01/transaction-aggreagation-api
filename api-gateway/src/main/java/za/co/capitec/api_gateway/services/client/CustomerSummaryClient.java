package za.co.capitec.api_gateway.services.client;

import jakarta.ws.rs.Path;
import org.springframework.web.bind.annotation.PathVariable;
import za.co.capitec.api_gateway.dto.CustomerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;
import za.co.capitec.api_gateway.dto.accountsTransactions.AccTransactionResponse;
import za.co.capitec.api_gateway.dto.accountsTransactions.AccountTransaction;

import java.util.List;

public interface CustomerSummaryClient {

    @GetExchange(value = "/capitec/customer/api/v1/{idNumber}",accept = "application/json")
    Mono<ResponseEntity<CustomerDto>> fetchCustomerDetails(@PathVariable(value = "idNumber") String idNumber);

    @GetExchange(value = "/capitec/accounts/api/v1/transaction/{idNumber}/{startDate}/{endDate}?pageNo=0&pageSize=10&sortBy=date&sortDir=asc",accept = "application/json")
    Mono<ResponseEntity<AccTransactionResponse>> fetchAccountTransactions(@PathVariable(value = "idNumber") String idNumber,
                                                                          @PathVariable(value = "startDate") String startDate,
                                                                          @PathVariable(value = "endDate") String endDate);

    @GetExchange(value = "/capitec/creditcards/api/v1/transaction/{idNumber}/{startDate}/{endDate}",accept = "application/json")
    Mono<ResponseEntity<List<AccountTransaction>>> fetchCreditCardTransactions(@PathVariable(value = "idNumber") String idNumber,
                                                                            @PathVariable(value = "startDate") String startDate,
                                                                            @PathVariable(value = "endDate") String endDate,
                                                                            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                            @RequestParam(value = "sortBy", defaultValue = "date", required = false) String sortBy,
                                                                            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir);

    @GetExchange(value = "/capitec/loans/api/v1/transaction/{idNumber}/{startDate}/{endDate}",accept = "application/json")
    Mono<ResponseEntity<List<AccountTransaction>>> fetchLoanTransactions(@PathVariable(value = "idNumber") String idNumber,
                                                                            @PathVariable(value = "startDate") String startDate,
                                                                            @PathVariable(value = "endDate") String endDate,
                                                                            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                            @RequestParam(value = "sortBy", defaultValue = "date", required = false) String sortBy,
                                                                            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir);

}
