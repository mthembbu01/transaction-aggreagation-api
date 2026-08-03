package za.co.capitec.accounts.entity;

import jakarta.persistence.*;
import lombok.*;
import za.co.capitec.coreapi.enums.accounts.AccountType;

import java.math.BigDecimal;


@Table(name = "accounts")
@Entity(name = "accounts")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Accounts extends BaseEntity {
    @Id
    @SequenceGenerator(name = "accounts_sequence",sequenceName = "accounts_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_sequence")
    private Long id;

    private Long accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountType accountType; // SAVINGS, CHEQUE, Transactional

    private BigDecimal balance = BigDecimal.ZERO;
    private String mobileNumber;
    private String idNumber;
    private String branchAddress;
    private boolean activeSw;

}
