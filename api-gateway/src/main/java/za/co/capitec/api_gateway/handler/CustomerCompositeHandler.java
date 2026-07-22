package za.co.capitec.api_gateway.handler;


import za.co.capitec.api_gateway.dto.*;
import za.co.capitec.api_gateway.services.client.CustomerSummaryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerCompositeHandler {

    private final CustomerSummaryClient customerSummaryClient;

    public Mono<ServerResponse> fetchCustomerSummary(ServerRequest request) {
        String mobileNumber = request.queryParam("mobileNumber").get();


        Mono<ResponseEntity<CustomerDto>> customerDetails = customerSummaryClient.fetchCustomerDetails(mobileNumber);
        Mono<ResponseEntity<AccountsDto>> accountsDetails = customerSummaryClient.fetchAccountDetails(mobileNumber);
        Mono<ResponseEntity<LoansDto>> loansDetails = customerSummaryClient.fetchLoanDetails(mobileNumber);
        Mono<ResponseEntity<CardsDto>> cardsDetails = customerSummaryClient.fetchCardDetails(mobileNumber);
        //-- return the combined results
        return Mono.zip(customerDetails,accountsDetails,loansDetails,cardsDetails)
                .flatMap(tuple -> {
                    CustomerDto customerDto = tuple.getT1().getBody();
                    AccountsDto accountsDto = tuple.getT2().getBody();
                    LoansDto loansDto = tuple.getT3().getBody();
                    CardsDto cardsDto = tuple.getT4().getBody();

                    CustomerSummaryDto customerSummaryDto = new CustomerSummaryDto(customerDto, accountsDto, loansDto, cardsDto);

                    return ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(customerSummaryDto));
                });
    }
}
