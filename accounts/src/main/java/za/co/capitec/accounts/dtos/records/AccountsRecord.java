package za.co.capitec.accounts.dtos.records;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.capitec.accounts.enums.AccountType;

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
