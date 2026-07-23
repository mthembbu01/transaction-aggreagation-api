package za.co.capitec.loans.entity;

import lombok.*;
import za.co.capitec.loans.enums.LoanStatus;
import za.co.capitec.loans.enums.LoanType;

import java.time.LocalDate;

//@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Loans extends BaseEntity {
    private Long id;
    private Long loanNumber;
    private LoanType loanType;     // PERSONAL, HOME, VEHICLE, BUSINESS, STUDENT
    private String mobileNumber;
    private String idNumber;
    private Double loanAmount;
    private Double outstandingBalance;
    private Double monthlyInstalment;
    private LocalDate startDate;
    private LocalDate endDate;
    private LoanStatus status;     // ACTIVE, CLOSED, DEFAULTED
    private boolean activeSw;
}

