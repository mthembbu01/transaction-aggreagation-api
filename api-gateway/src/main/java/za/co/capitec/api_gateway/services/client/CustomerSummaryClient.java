package za.co.capitec.api_gateway.services.client;

import jakarta.ws.rs.Path;
import org.springframework.web.bind.annotation.PathVariable;
import za.co.capitec.api_gateway.dto.AccountsDto;
import za.co.capitec.api_gateway.dto.CustomerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;
import za.co.capitec.api_gateway.dto.accountsTransactions.AccountTransaction;

import java.util.List;

public interface CustomerSummaryClient {

    @GetExchange(value = "/capitec/customer/api/v1/{idNumber}",accept = "application/json")
    Mono<ResponseEntity<CustomerDto>> fetchCustomerDetails(@PathVariable(value = "idNumber") String idNumber);

    @GetExchange(value = "/capitec/accounts/api/v1/{idNumber}",accept = "application/json")
    Mono<ResponseEntity<List<AccountTransaction>>> fetchAccountTransactions(@PathVariable(value = "idNumber") String idNumber,
                                                                            @RequestParam(value = "startDate") String startDate,
                                                                            @RequestParam(value = "endDate") String endDate);

//    @GetExchange(value = "/eazybank/loans/api/fetch",accept = "application/json")
//    Mono<ResponseEntity<LoanTransaction>> fetchLoanDetails(@RequestParam(value = "idNumber") String idNumber);
//
//    @GetExchange(value = "/eazybank/cards/api/fetch",accept = "application/json")
//    Mono<ResponseEntity<CardsDto>> fetchCardDetails(@RequestParam(value = "idNumber") String idNumber);
}
