package za.co.capitec.api_gateway.handler;


import za.co.capitec.api_gateway.dto.*;
import za.co.capitec.api_gateway.dto.accountsTransactions.AccountTransaction;
import za.co.capitec.api_gateway.services.client.CustomerSummaryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerCompositeHandler {

    private final CustomerSummaryClient customerSummaryClient;

    public Mono<ServerResponse> fetchCustomerSummary(ServerRequest request) {
        String idNumber = request.queryParam("idNumber").get();
        String startDate = request.queryParam("startDate").get();
        String endDate = request.queryParam("endDate").get();

        Mono<ResponseEntity<CustomerDto>> customerDetails = customerSummaryClient.fetchCustomerDetails(idNumber);
        Mono<ResponseEntity<List<AccountTransaction>>> accountsTransactions = customerSummaryClient.fetchAccountTransactions(idNumber,startDate,endDate);
//        Mono<ResponseEntity<LoanTransaction>> loansDetails = customerSummaryClient.fetchLoanDetails(idNumber);
//        Mono<ResponseEntity<CardsDto>> cardsDetails = customerSummaryClient.fetchCardDetails(idNumber);
        //-- return the combined results
        return Mono.zip(customerDetails,accountsTransactions, null, null)
                .flatMap(tuple -> {
                    CustomerDto customerDto = tuple.getT1().getBody();
                    List<AccountTransaction> accountsDto = tuple.getT2().getBody();
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
