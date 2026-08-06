package za.co.capitec.api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import za.co.capitec.coreapi.dtos.accounts.response.TransactionResponse;
import za.co.capitec.coreapi.dtos.creditcards.response.CreditCardTransactionResponse;
import za.co.capitec.coreapi.dtos.customer.records.CustomersRecord;
import za.co.capitec.coreapi.dtos.loans.response.LoanTransactionResponse;

@Data
@AllArgsConstructor
public class CustomerSummaryDto {
    private CustomersRecord customersRecord;
    private TransactionResponse accountTransactionResponse; //-- AccountTransactions to be exact
    private CreditCardTransactionResponse creditCardTransactionResponse;
    private LoanTransactionResponse loanTransactionResponse;
}
