package za.co.capitec.api_gateway.handler;


import za.co.capitec.api_gateway.dto.*;
import za.co.capitec.api_gateway.dto.accountsTransactions.AccTransactionResponse;
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

@Component
@RequiredArgsConstructor
public class CustomerCompositeHandler {

    private final CustomerTransactionsSummaryClient customerTransactionsSummaryClient;

    public Mono<ServerResponse> fetchCustomerSummary(ServerRequest request) {
        String idNumber = request.queryParam("idNumber").get();
        String startDate = request.queryParam("startDate").get();
        String endDate = request.queryParam("endDate").get();

        Mono<ResponseEntity<CustomersRecord>> customerDetails = customerTransactionsSummaryClient.fetchCustomerDetails(idNumber);
        Mono<ResponseEntity<TransactionResponse>> accountsTransactions = customerTransactionsSummaryClient.fetchAccountTransactions(idNumber,startDate,endDate);
        Mono<ResponseEntity<CreditCardTransactionResponse>> ccTransactions = customerTransactionsSummaryClient.fetchCardDetails(idNumber);
//        Mono<ResponseEntity<LoanTransaction>> loansDetails = customerTransactionsSummaryClient.fetchLoanDetails(idNumber);

        //-- return the combined results
        return Mono.zip(customerDetails,accountsTransactions, null, null)
                .flatMap(tuple -> {
                    CustomersRecord customerDto = tuple.getT1().getBody();
                    TransactionResponse accountsDto = tuple.getT2().getBody();
//                    LoanTransaction loansDto = tuple.getT3().getBody();
//                    CardsDto cardsDto = tuple.getT4().getBody();

                    CustomerSummaryDto customerSummaryDto = new CustomerSummaryDto(customerDto, accountsDto, null, null);

                    return ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(customerSummaryDto));
                });
    }
}
