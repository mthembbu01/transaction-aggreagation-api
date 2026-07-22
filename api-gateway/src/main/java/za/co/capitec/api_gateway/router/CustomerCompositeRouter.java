package za.co.capitec.api_gateway.router;


import za.co.capitec.api_gateway.handler.CustomerCompositeHandler;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class CustomerCompositeRouter {
    @Bean
    public RouterFunction<ServerResponse> route(CustomerCompositeHandler compositeHandler, ParameterNamesModule parameterNamesModule) {
        return RouterFunctions.route(RequestPredicates.GET("/api/composite/fetchCustomerSummary")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON))
                                .and(RequestPredicates.queryParam("mobileNumber", param -> true))
                        .and(RequestPredicates.queryParam("accountNumber", param -> true))
                , compositeHandler::fetchCustomerSummary);
    }
}
