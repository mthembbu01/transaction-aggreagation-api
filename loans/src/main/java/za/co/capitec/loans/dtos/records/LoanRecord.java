package za.co.capitec.loans.dtos.records;

import za.co.capitec.loans.enums.LoanStatus;
import za.co.capitec.loans.enums.LoanType;

import java.time.LocalDate;

public record LoanRecord(Long loanNumber,
                          LoanType loanType,
                          String mobileNumber,
                          String idNumber,
                          Double loanAmount,
                          Double outstandingBalance,
                          Double monthlyInstalment,
                          LocalDate startDate,
                          LocalDate endDate,
                          LoanStatus status,
                          boolean activeSw) {
}

