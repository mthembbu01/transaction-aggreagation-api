package za.co.capitec.loans.entity;

import jakarta.persistence.*;
import lombok.*;
import za.co.capitec.loans.enums.Categories;

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

    private String description;
    private Double amount;
    private Boolean isImmediate = Boolean.FALSE;
    private String reference;
    private LocalTime time;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "loan_number")
    private Loans loan;
}
