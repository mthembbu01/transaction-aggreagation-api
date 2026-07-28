package za.co.capitec.loans.entity;

import jakarta.persistence.*;
import lombok.*;
import za.co.capitec.loans.enums.Categories;
import za.co.capitec.loans.utilities.dates.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "loan_transactions")
@Entity(name = "loan_transactions")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class LoanTransactions {

    @Id
    @SequenceGenerator(name = "loan_transactions_sequence", sequenceName = "loan_transactions_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_transactions_sequence")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Categories category;

    private BigDecimal amount;
    private String reference;
    private LocalTime time;
    private LocalDate date;

    public static LoanTransactions create(Loans loan, BigDecimal amount, Categories category, String reference){
        return LoanTransactions
                .builder()
                .loan(loan)
                .amount(amount)
                .category(category)
                .reference(reference)
                .date(DateUtils.getCurrentDate())
                .time(DateUtils.getCurrentTime())
                .build();
    }

    @ManyToOne
    @JoinColumn(name = "loan_id", referencedColumnName = "id")
    private Loans loan;
}
