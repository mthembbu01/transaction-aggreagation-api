package za.co.capitec.api_gateway.handler;


import org.springframework.lang.NonNull;
import reactor.util.function.Tuple4;
import za.co.capitec.api_gateway.dto.*;
import za.co.capitec.api_gateway.services.client.CustomerTransactionsSummaryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import za.co.capitec.coreapi.dtos.accounts.response.TransactionResponse;
import za.co.capitec.coreapi.dtos.creditcards.response.CreditCardTransactionResponse;
import za.co.capitec.coreapi.dtos.customer.records.CustomersRecord;
import za.co.capitec.coreapi.dtos.loans.response.LoanTransactionResponse;

@Component
@RequiredArgsConstructor
public class CustomerTransactionCompositeHandler {

    private final CustomerTransactionsSummaryClient customerTransactionsSummaryClient;

    public Mono<ServerResponse> fetchCustomerSummary(ServerRequest request) {
        //-- 1. Fetch the customer fields from the request parameters
        String idNumber = request.queryParam("idNumber").get();
        String startDate = request.queryParam("startDate").get();
        String endDate = request.queryParam("endDate").get();

        //-- 2. Get the customer details and transactions from different services, perform aggregation
        Mono<ResponseEntity<CustomersRecord>> customerDetails = customerTransactionsSummaryClient.fetchCustomerDetails(idNumber);
        Mono<ResponseEntity<TransactionResponse>> accountsTransactions = customerTransactionsSummaryClient.fetchAccountTransactions(idNumber,startDate,endDate);
        Mono<ResponseEntity<CreditCardTransactionResponse>> ccTransactions = customerTransactionsSummaryClient.fetchCreditCardTransactions(idNumber,startDate,endDate);
        Mono<ResponseEntity<LoanTransactionResponse>> loanTransactions = customerTransactionsSummaryClient.fetchLoanTransactions(idNumber,startDate,endDate);

        //-- 3. Combine and aggregate the results and return a CustomerSummaryDto
        return Mono.zip(customerDetails,accountsTransactions, ccTransactions, loanTransactions)
                .flatMap(tuple -> {
                    CustomersRecord customersRecord = tuple.getT1().getBody();
                    CustomerSummaryDto customerSummaryDto = getCustomerSummaryDto(tuple, customersRecord);
                    //-- server response for
                    return ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(customerSummaryDto));
                });
    }
    /**
     *
     * @param tuple
     * @param customersRecord
     * @return
     */
    @NonNull
    private static CustomerSummaryDto getCustomerSummaryDto(Tuple4<ResponseEntity<CustomersRecord>, ResponseEntity<TransactionResponse>, ResponseEntity<CreditCardTransactionResponse>, ResponseEntity<LoanTransactionResponse>> tuple, CustomersRecord customersRecord) {
        TransactionResponse transactionResponse = tuple.getT2().getBody();
        CreditCardTransactionResponse creditCardTransactionResponse = tuple.getT3().getBody();
        LoanTransactionResponse loanTransactionResponse = tuple.getT4().getBody();
        //-- build the transaction's summary
        return new CustomerSummaryDto(customersRecord, transactionResponse, creditCardTransactionResponse, loanTransactionResponse);
    }
}
