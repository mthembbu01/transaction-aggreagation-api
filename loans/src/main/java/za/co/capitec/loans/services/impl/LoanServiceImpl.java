package za.co.capitec.loans.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import za.co.capitec.loans.constants.LoansConstants;
import za.co.capitec.loans.dtos.records.LoanRecord;
import za.co.capitec.loans.dtos.requests.CreateLoanDto;
import za.co.capitec.loans.dtos.requests.UpdateLoanDto;
import za.co.capitec.loans.dtos.response.ResponseDto;
import za.co.capitec.loans.entity.Loans;
import za.co.capitec.loans.enums.LoanStatus;
import za.co.capitec.loans.exceptions.ResourceAlreadyExistsException;
import za.co.capitec.loans.exceptions.ResourceNotFoundException;
import za.co.capitec.loans.repositories.LoanRepository;
import za.co.capitec.loans.services.ILoanService;
import za.co.capitec.loans.utilities.LoanUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements ILoanService {

    private final LoanRepository loanRepository;

    private final ModelMapper modelMapper;
    @Override
    public ResponseDto createLoan(CreateLoanDto createLoanDto) {
        //-- 1. Map the CreateLoanDto to Loans
        Loans loan = modelMapper.map(createLoanDto, Loans.class);
        //-- If one of the customer unique attributes exists, it becomes an unprocessable entity
        isExist(createLoanDto.getIdNumber(), createLoanDto.getMobileNumber());
        //-- 2. Save the newly created loan
        loan.setLoanNumber(Long.parseLong(LoanUtils.generateLoanNumber()));
        loan.setOutstandingBalance(createLoanDto.getLoanAmount());
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setActiveSw(true);
        loanRepository.save(loan);
        //-- 3. Return the response
        return new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201);
    }
    /**
     * Find Loan by loanNumber
     * @param loanNumber
     * @return
     */
    @Override
    public LoanRecord findByLoanNumber(Long loanNumber) {
        //-- 1. Find the loan by loanNumber
        Loans loan = findLoan(loanNumber);
        //-- 2. Return the Loan Record
        return modelMapper.map(loan, LoanRecord.class);
    }

    /**
     *
     * @param idNumber
     * @return
     */
    @Override
    public List<LoanRecord> findLoansByIdNumber(String idNumber) {
        //-- 1. does the loan exist by idNumber
        if (!loanRepository.existsByIdNumber(idNumber))
            throw new ResourceNotFoundException("Loan", "ID Number", idNumber);
        //-- 1. Find loans by ID number
        return loanRepository.findAllByIdNumber(idNumber)
                .stream()
                .filter(Loans::isActiveSw)
                .map(loan -> modelMapper.map(loan, LoanRecord.class))
                .collect(Collectors.toList());
    }
    /**
     *
     * @param loanNumber
     * @param updateLoanDto
     * @return
     */
    @Override
    public ResponseDto updateLoanByLoanNumber(Long loanNumber, UpdateLoanDto updateLoanDto) {
        //-- 1. Find the loan by loanNumber
        Loans loan = findLoan(loanNumber);
        //-- 2. Extract update values
        String mobileNumber = updateLoanDto.getMobileNumber();
        String idNumber = updateLoanDto.getIdNumber();
        //-- If one of the customer unique attributes exists, it becomes an unprocessable entity
        isExist(idNumber, mobileNumber);
        //-- 4. Update attributes
        loan.setMobileNumber(mobileNumber);
        loan.setIdNumber(idNumber);
        loan.setLoanAmount(updateLoanDto.getLoanAmount());
        loan.setMonthlyInstalment(updateLoanDto.getMonthlyInstalment());
        loan.setEndDate(updateLoanDto.getEndDate());
        loan.setStatus(updateLoanDto.getStatus());
        loan.setActiveSw(updateLoanDto.isActiveSw());
        //-- update the loan object
        loanRepository.save(loan);
        //-- 5. return a proper message
        return new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200);
    }
    /**
     *
     * @param loan
     * @return
     */
    @Override
    public ResponseDto saveLoan(Loans loan) {
        loanRepository.save(loan);
        return new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200);
    }

    /**
     *
     * @param loanNumber
     * @return
     */
    @Override
    public ResponseDto deleteLoanByLoanNumber(Long loanNumber) {
        //-- 1. Find the loan by loanNumber
        Loans loan = findLoan(loanNumber);
        //-- 2. Soft delete
        loan.setOutstandingBalance(BigDecimal.ZERO);
        loan.setActiveSw(false);
        loan.setStatus(LoanStatus.CLOSED);
        //-- update the loan object
        loanRepository.save(loan);
        //-- 3. return a proper message
        return new ResponseDto(LoansConstants.STATUS_204, LoansConstants.MESSAGE_204);
    }
    /**
     * Check whether the Loan with unique fields - ID Number, Mobile Number - already exists
     * @param idNumber
     * @param mobileNumber
     * @return
     */
    private void isExist(String idNumber, String mobileNumber) {
        boolean isExistByIdNumber = loanRepository.existsByIdNumber(idNumber);
        boolean isExistByMobileNumber = loanRepository.existsByMobileNumber(mobileNumber);
        //-- ID number exists
        if (isExistByIdNumber) {
            throw new ResourceAlreadyExistsException("Loan", "ID Number", idNumber);
        }
        //-- check if mobile Number exists
        if (isExistByMobileNumber) {
            throw new ResourceAlreadyExistsException("Loan", "Mobile Number", mobileNumber);
        }
    }
    /**
     * Finds Loans by Loan Number
     * @param loanNumber
     * @return
     */
    @Override
    public Loans findLoan(Long loanNumber) {
        return loanRepository.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "Loan Number", String.valueOf(loanNumber)));
    }
}

