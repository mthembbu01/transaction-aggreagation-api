package za.co.capitec.api_gateway.handler;

/*
 * API Composite Pattern - Below is a class that applies the API composite pattern for multiple API aggregation
 */

import org.springframework.lang.NonNull;
import org.springframework.http.HttpStatus;
import reactor.util.function.Tuple4;
import za.co.capitec.api_gateway.dto.*;
import za.co.capitec.api_gateway.services.client.CustomerTransactionsSummaryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import za.co.capitec.coreapi.dtos.accounts.response.TransactionResponse;
import za.co.capitec.coreapi.dtos.creditcards.response.CreditCardTransactionResponse;
import za.co.capitec.coreapi.dtos.customer.records.CustomersRecord;
import za.co.capitec.coreapi.dtos.loans.response.LoanTransactionResponse;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomerTransactionCompositeHandler {

    private final CustomerTransactionsSummaryClient customerTransactionsSummaryClient;

    /**
     *
     * @param request
     * @return
     */
    public Mono<ServerResponse> fetchCustomerSummary(ServerRequest request) {
        try {
            //-- 1. Fetch and validate customer fields from request parameters
            String idNumber = requiredQueryParam(request, "idNumber");
            String startDate = requiredQueryParam(request, "startDate");
            String endDate = requiredQueryParam(request, "endDate");

            //-- 2. Get customer details and transactions from downstream services
            Mono<ResponseEntity<CustomersRecord>> customerDetails = customerTransactionsSummaryClient.fetchCustomerDetails(idNumber);
            Mono<ResponseEntity<TransactionResponse>> accountsTransactions = customerTransactionsSummaryClient.fetchAccountTransactions(idNumber,startDate,endDate);
            Mono<ResponseEntity<CreditCardTransactionResponse>> ccTransactions = customerTransactionsSummaryClient.fetchCreditCardTransactions(idNumber,startDate,endDate);
            Mono<ResponseEntity<LoanTransactionResponse>> loanTransactions = customerTransactionsSummaryClient.fetchLoanTransactions(idNumber,startDate,endDate);

            //-- 3. Aggregate and return composite response
            return Mono.zip(customerDetails,accountsTransactions, ccTransactions, loanTransactions)
                    .flatMap(tuple -> {
                        CustomersRecord customersRecord = requireBody(tuple.getT1(), "customer details");
                        CustomerSummaryDto customerSummaryDto = getCustomerSummaryDto(tuple, customersRecord);
                        return ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(customerSummaryDto));
                    })
                    .onErrorResume(WebClientResponseException.class, ex ->
                            ServerResponse
                                    .status(ex.getStatusCode())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(Map.of("error", "Downstream service error", "status", ex.getStatusCode().value(), "message", ex.getMessage())))
                    .onErrorResume(IllegalArgumentException.class, ex ->
                            ServerResponse
                                    .badRequest()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(Map.of("error", "Invalid request", "message", ex.getMessage())))
                    .onErrorResume(Exception.class, ex ->
                            ServerResponse
                                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(Map.of("error", "Internal error", "message", "Unable to build customer summary")));
        } catch (IllegalArgumentException ex) {
            return ServerResponse
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("error", "Invalid request", "message", ex.getMessage()));
        }
    }

    private static String requiredQueryParam(ServerRequest request, String paramName) {
        return request.queryParam(paramName)
                .filter(value -> !value.isBlank())
                .orElseThrow(() -> new IllegalArgumentException("Missing required query param: " + paramName));
    }

    private static <T> T requireBody(ResponseEntity<T> response, String label) {
        if (response == null || response.getBody() == null) {
            throw new IllegalArgumentException("Empty response body from " + label);
        }
        return response.getBody();
    }
    /**
     * Builds the customer summary DTO from aggregated downstream responses.
     * @param tuple responses from account/card/loan/customer services
     * @param customersRecord customer details body
     * @return fully aggregated customer summary
     */
    @NonNull
    private static CustomerSummaryDto getCustomerSummaryDto(Tuple4<ResponseEntity<CustomersRecord>, ResponseEntity<TransactionResponse>, ResponseEntity<CreditCardTransactionResponse>, ResponseEntity<LoanTransactionResponse>> tuple, CustomersRecord customersRecord) {
        TransactionResponse transactionResponse = requireBody(tuple.getT2(), "account transactions");
        CreditCardTransactionResponse creditCardTransactionResponse = requireBody(tuple.getT3(), "credit card transactions");
        LoanTransactionResponse loanTransactionResponse = requireBody(tuple.getT4(), "loan transactions");
        //-- build the transaction's summary
        return new CustomerSummaryDto(customersRecord, transactionResponse, creditCardTransactionResponse, loanTransactionResponse);
    }
}
