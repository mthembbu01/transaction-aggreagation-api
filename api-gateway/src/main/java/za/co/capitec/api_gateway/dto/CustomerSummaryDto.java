package za.co.capitec.api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import za.co.capitec.api_gateway.dto.accountsTransactions.AccTransactionResponse;
import za.co.capitec.api_gateway.dto.loansTransactions.LoanTransaction;

@Data
@AllArgsConstructor
public class CustomerSummaryDto {
    private CustomerDto customer;
    private AccTransactionResponse accountsTransactions;
    private LoanTransaction loansTransactions;
    private CardsDto card;

}
