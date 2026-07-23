package za.co.capitec.api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import za.co.capitec.api_gateway.dto.accountsTransactions.AccountTransaction;

import java.util.List;

@Data
@AllArgsConstructor
public class CustomerSummaryDto {
    private CustomerDto customer;
    private List<AccountTransaction> accountsTransactions;
    private LoanTransaction loansTransactions;
    private CardsDto card;

}
