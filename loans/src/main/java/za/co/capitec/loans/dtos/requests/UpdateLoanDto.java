package za.co.capitec.loans.dtos.requests;

import za.co.capitec.loans.enums.LoanStatus;
import za.co.capitec.loans.enums.LoanType;

import java.time.LocalDate;

public record UpdateLoanDto(String mobileNumber,
                             String idNumber,
                             Double loanAmount,
                             Double monthlyInstalment,
                             LocalDate endDate,
                             LoanStatus status,
                             boolean activeSw) {
}

