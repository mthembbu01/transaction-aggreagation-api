package za.co.capitec.coreapi.dtos.accounts.records;


import lombok.*;
import za.co.capitec.coreapi.enums.accounts.AccountType;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountsRecord {
    private Long accountNumber;
    private AccountType accountType; // SAVINGS, CHEQUE, Transactional, Business
    private String mobileNumber;
    private String idNumber;
    private String branchAddress;
    private boolean activeSw;
}
