package za.co.capitec.accounts.entity;

import lombok.*;
import za.co.capitec.accounts.enums.AccountType;

//@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Accounts extends BaseEntity {
    private Long id;
    private Long accountNumber;
    private AccountType accountType; // SAVINGS, CHEQUE, Transactional
    private String mobileNumber;
    private String idNumber;
    private String branchAddress;
    private boolean activeSw;

}
