package za.co.capitec.loans.dtos.requests;

import jakarta.validation.constraints.Pattern;
import za.co.capitec.loans.enums.LoanType;

import java.time.LocalDate;

public record CreateLoanDto(LoanType loanType,
                             @Pattern(regexp = "^(\\+27|0)?[1-9]\\d{8}$", message = "Invalid mobile number")
                             String mobileNumber,
                             @Pattern(regexp = "^\\d{13}$", message = "Invalid ID Number")
                             String idNumber,
                             Double loanAmount,
                             Double monthlyInstalment,
                             LocalDate startDate,
                             LocalDate endDate,
                             boolean activeSw) {
}

