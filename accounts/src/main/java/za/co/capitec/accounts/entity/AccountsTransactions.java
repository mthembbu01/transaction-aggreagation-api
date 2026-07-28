package za.co.capitec.accounts.entity;


import jakarta.persistence.*;
import lombok.*;
import za.co.capitec.accounts.enums.AccountType;
import za.co.capitec.accounts.enums.Categories;
import za.co.capitec.accounts.utilities.dates.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "accounts_transactions")
@Entity(name = "accounts_transactions")
@Builder
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class AccountsTransactions {
    @Id
    @SequenceGenerator(name = "account_transactions_sequence",sequenceName = "account_transactions_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_transactions_sequence")
    private Long id;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private Categories category;

    private String description;
    private BigDecimal amount;
    private Boolean isImmediate;
    private String reference;
    private LocalTime time;
    private LocalDate date;

    /**
     * The constructor to create a transaction
     * @param accounts
     * @param description
     * @param isImmediate
     * @param amount
     * @param category
     * @param reference
     * @return
     */
    public static AccountsTransactions create(Accounts accounts, String description, Boolean isImmediate, BigDecimal amount, Categories category, String reference){
        return AccountsTransactions
                .builder()
                .accounts(accounts)
                .accountType(accounts.getAccountType())
                .description(description)
                .isImmediate(isImmediate)
                .amount(amount)
                .category(category)
                .reference(reference)
                .date(DateUtils.getCurrentDate())
                .time(DateUtils.getCurrentTime())
                .build();
    }

    @ManyToOne
    @JoinColumn(name = "account_number")
    private Accounts accounts;
}
