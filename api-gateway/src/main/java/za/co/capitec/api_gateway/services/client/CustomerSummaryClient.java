package za.co.capitec.api_gateway.services.client;

import za.co.capitec.api_gateway.dto.AccountsDto;
import za.co.capitec.api_gateway.dto.CardsDto;
import za.co.capitec.api_gateway.dto.CustomerDto;
import za.co.capitec.api_gateway.dto.LoansDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface CustomerSummaryClient {

    @GetExchange(value = "/eazybank/customer/api/fetch",accept = "application/json")
    Mono<ResponseEntity<CustomerDto>> fetchCustomerDetails(@RequestParam(value = "mobileNumber") String mobileNumber);

    @GetExchange(value = "/eazybank/accounts/api/fetch",accept = "application/json")
    Mono<ResponseEntity<AccountsDto>> fetchAccountDetails(@RequestParam(value = "mobileNumber") String mobileNumber);

    @GetExchange(value = "/eazybank/loans/api/fetch",accept = "application/json")
    Mono<ResponseEntity<LoansDto>> fetchLoanDetails(@RequestParam(value = "mobileNumber") String mobileNumber);

    @GetExchange(value = "/eazybank/cards/api/fetch",accept = "application/json")
    Mono<ResponseEntity<CardsDto>> fetchCardDetails(@RequestParam(value = "mobileNumber") String mobileNumber);
}
