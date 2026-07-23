package za.co.capitec.loans.entity;

import lombok.*;
import za.co.capitec.loans.enums.Categories;

import java.time.LocalDate;
import java.time.LocalTime;

//@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoanTransactions {
    private Long id;
    private Long loanNumber;
    private String description;
    private Double amount;
    private Boolean isImmediate = Boolean.FALSE;
    private LocalTime time;
    private LocalDate date;
    private Categories category;
    private String reference;
}

