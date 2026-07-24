package za.co.capitec.loans.services;

import za.co.capitec.loans.dtos.records.LoanRecord;
import za.co.capitec.loans.dtos.requests.CreateLoanDto;
import za.co.capitec.loans.dtos.requests.UpdateLoanDto;
import za.co.capitec.loans.dtos.response.ResponseDto;

import java.util.List;

public interface ILoanService {
	LoanRecord findByLoanNumber(Long loanNumber);
	List<LoanRecord> findLoansByIdNumber(String idNumber);
	ResponseDto createLoan(CreateLoanDto createLoanDto);
	ResponseDto updateLoanByLoanNumber(Long loanNumber, UpdateLoanDto updateLoanDto);
	ResponseDto deleteLoanByLoanNumber(Long loanNumber);
}

