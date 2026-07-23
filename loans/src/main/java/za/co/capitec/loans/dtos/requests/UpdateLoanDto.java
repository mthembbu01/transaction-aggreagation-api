package za.co.capitec.loans.dtos.requests;

import za.co.capitec.loans.enums.LoanStatus;
import za.co.capitec.loans.enums.LoanType;

import java.time.LocalDate;

public record UpdateLoanDto(Long loanNumber,
                             LoanType loanType,
                             String mobileNumber,
                             String idNumber,
                             Double loanAmount,
                             Double monthlyInstalment,
                             LocalDate startDate,
                             LocalDate endDate,
                             LoanStatus status,
                             boolean activeSw) {
}

