package za.co.capitec.loans.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import za.co.capitec.loans.constants.LoansConstants;
import za.co.capitec.loans.dtos.records.LoanRecord;
import za.co.capitec.loans.dtos.requests.CreateLoanDto;
import za.co.capitec.loans.dtos.requests.UpdateLoanDto;
import za.co.capitec.loans.dtos.response.LoansResponse;
import za.co.capitec.loans.dtos.response.ResponseDto;
import za.co.capitec.loans.entity.Loans;
import za.co.capitec.loans.enums.LoanStatus;
import za.co.capitec.loans.exceptions.ResourceAlreadyExistsException;
import za.co.capitec.loans.exceptions.ResourceNotFoundException;
import za.co.capitec.loans.repositories.LoanRepository;
import za.co.capitec.loans.services.ILoanService;
import za.co.capitec.loans.utilities.LoanUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements ILoanService {

    private final LoanRepository loanRepository;

    @Override
    public LoansResponse findAll(Pageable pageable) {
        Page<Loans> page = loanRepository.findAll(pageable);
        return LoansResponse.builder()
                .content(page.getContent().stream().map(this::toRecord).toList())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .isLast(page.isLast())
                .build();
    }

    @Override
    public LoanRecord findByLoanNumber(Long loanNumber) {
        Loans loan = loanRepository.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "loanNumber", String.valueOf(loanNumber)));
        return toRecord(loan);
    }

    @Override
    public List<LoanRecord> findLoansByIdNumber(String idNumber) {
        if (!loanRepository.existsByIdNumber(idNumber)) {
            throw new ResourceNotFoundException("Loan", "idNumber", idNumber);
        }

        return loanRepository.findAllByIdNumber(idNumber).stream()
                .filter(Loans::isActiveSw)
                .map(this::toRecord)
                .toList();
    }

    @Override
    public ResponseDto createLoan(CreateLoanDto createLoanDto) {
        validateUniqueFields(createLoanDto.idNumber(), createLoanDto.mobileNumber());

        Loans loan = Loans.builder()
                .loanNumber(Long.parseLong(LoanUtils.generateLoanNumber()))
                .loanType(createLoanDto.loanType())
                .mobileNumber(createLoanDto.mobileNumber())
                .idNumber(createLoanDto.idNumber())
                .loanAmount(createLoanDto.loanAmount())
                .outstandingBalance(createLoanDto.loanAmount())
                .monthlyInstalment(createLoanDto.monthlyInstalment())
                .startDate(createLoanDto.startDate())
                .endDate(createLoanDto.endDate())
                .status(LoanStatus.ACTIVE)
                .activeSw(true)
                .build();

        loanRepository.save(loan);

        return ResponseDto.builder()
                .statusCode(LoansConstants.STATUS_201)
                .statusMsg(LoansConstants.MESSAGE_201)
                .build();
    }

    @Override
    public ResponseDto updateLoanByLoanNumber(Long loanNumber, UpdateLoanDto updateLoanDto) {
        Loans loan = loanRepository.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "loanNumber", String.valueOf(loanNumber)));

        loan.setLoanType(updateLoanDto.loanType());
        loan.setMobileNumber(updateLoanDto.mobileNumber());
        loan.setIdNumber(updateLoanDto.idNumber());
        loan.setLoanAmount(updateLoanDto.loanAmount());
        loan.setMonthlyInstalment(updateLoanDto.monthlyInstalment());
        loan.setStartDate(updateLoanDto.startDate());
        loan.setEndDate(updateLoanDto.endDate());
        loan.setStatus(updateLoanDto.status());
        loan.setActiveSw(updateLoanDto.activeSw());

        loanRepository.save(loan);

        return ResponseDto.builder()
                .statusCode(LoansConstants.STATUS_200)
                .statusMsg(LoansConstants.MESSAGE_200)
                .build();
    }

    @Override
    public ResponseDto deleteLoanByLoanNumber(Long loanNumber) {
        Loans loan = loanRepository.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "loanNumber", String.valueOf(loanNumber)));

        loan.setActiveSw(false);
        loanRepository.save(loan);

        return ResponseDto.builder()
                .statusCode(LoansConstants.STATUS_204)
                .statusMsg(LoansConstants.MESSAGE_204)
                .build();
    }

    private void validateUniqueFields(String idNumber, String mobileNumber) {
        if (loanRepository.existsByIdNumber(idNumber)) {
            throw new ResourceAlreadyExistsException("Loan", "idNumber", idNumber);
        }
        if (loanRepository.existsByMobileNumber(mobileNumber)) {
            throw new ResourceAlreadyExistsException("Loan", "mobileNumber", mobileNumber);
        }
    }

    private LoanRecord toRecord(Loans loan) {
        return new LoanRecord(
                loan.getLoanNumber(),
                loan.getLoanType(),
                loan.getMobileNumber(),
                loan.getIdNumber(),
                loan.getLoanAmount(),
                loan.getOutstandingBalance(),
                loan.getMonthlyInstalment(),
                loan.getStartDate(),
                loan.getEndDate(),
                loan.getStatus(),
                loan.isActiveSw()
        );
    }
}

