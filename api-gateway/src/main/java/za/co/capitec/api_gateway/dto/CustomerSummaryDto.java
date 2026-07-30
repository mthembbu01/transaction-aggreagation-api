package za.co.capitec.api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import za.co.capitec.coreapi.dtos.accounts.response.TransactionResponse;
import za.co.capitec.coreapi.dtos.creditcards.response.CreditCardTransactionResponse;
import za.co.capitec.coreapi.dtos.customer.records.CustomersRecord;

@Data
@AllArgsConstructor
public class CustomerSummaryDto {
    private CustomersRecord customersRecord;
    private TransactionResponse transactionResponse; //-- AccountTransactions to be exact
    private CreditCardTransactionResponse creditCardTransactionResponse;

    //TODO: Add the loan transactions

}
