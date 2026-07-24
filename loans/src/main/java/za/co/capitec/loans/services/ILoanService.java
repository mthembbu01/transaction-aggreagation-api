package za.co.capitec.loans.services;

import org.springframework.data.domain.Pageable;
import za.co.capitec.loans.dtos.records.LoanRecord;
import za.co.capitec.loans.dtos.requests.CreateLoanDto;
import za.co.capitec.loans.dtos.requests.UpdateLoanDto;
import za.co.capitec.loans.dtos.response.LoansResponse;
import za.co.capitec.loans.dtos.response.ResponseDto;

import java.util.List;

public interface ILoanService {
	LoansResponse findAll(Pageable pageable);
	LoanRecord findByLoanNumber(Long loanNumber);
	List<LoanRecord> findLoansByIdNumber(String idNumber);
	ResponseDto createLoan(CreateLoanDto createLoanDto);
	ResponseDto updateLoanByLoanNumber(Long loanNumber, UpdateLoanDto updateLoanDto);
	ResponseDto deleteLoanByLoanNumber(Long loanNumber);
}

