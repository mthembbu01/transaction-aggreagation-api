package za.co.capitec.coreapi.dtos.loans.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.capitec.coreapi.enums.loans.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLoanDto {
    private String mobileNumber;
    private String idNumber;
    private BigDecimal loanAmount;
    private BigDecimal monthlyInstalment;
    private LocalDate endDate;
    private LoanStatus status;
    private boolean activeSw;
}

