package za.co.capitec.coreapi.dtos.loans.records;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.capitec.coreapi.enums.loans.LoanStatus;
import za.co.capitec.coreapi.enums.loans.LoanType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanRecord {
    private Long loanNumber;
    private LoanType loanType;
    private String mobileNumber;
    private String idNumber;
    private Double loanAmount;
    private Double outstandingBalance;
    private Double monthlyInstalment;
    private LocalDate startDate;
    private LocalDate endDate;
    private LoanStatus status;
    private boolean activeSw;
}

