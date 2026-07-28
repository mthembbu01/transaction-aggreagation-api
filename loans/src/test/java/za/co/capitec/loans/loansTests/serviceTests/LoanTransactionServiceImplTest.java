package za.co.capitec.loans.loansTests.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import za.co.capitec.loans.dtos.requests.LoanTransactionDto;
import za.co.capitec.loans.dtos.response.LoanTransactionResponse;
import za.co.capitec.loans.dtos.response.ResponseDto;
import za.co.capitec.loans.entity.LoanTransactions;
import za.co.capitec.loans.entity.Loans;
import za.co.capitec.loans.enums.Categories;
import za.co.capitec.loans.enums.LoanStatus;
import za.co.capitec.loans.enums.LoanType;
import za.co.capitec.loans.exceptions.InsufficientFundsException;
import za.co.capitec.loans.repositories.LoanTransactionRepository;
import za.co.capitec.loans.services.ILoanService;
import za.co.capitec.loans.services.impl.LoanTransactionServiceImpl;
import za.co.capitec.loans.utils.LoansUtilities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanTransactionServiceImplTest {

    private LoanTransactionServiceImpl underTestService;

    @Mock
    private LoanTransactionRepository transactionRepository;

    @Mock
    private ILoanService loanService;

    @Captor
    private ArgumentCaptor<LoanTransactions> transactionCaptor;

    private Loans loan;

    @BeforeEach
    void setUp() {
        underTestService = new LoanTransactionServiceImpl(transactionRepository, loanService);

        loan = Loans.builder()
                .id(1L)
                .loanNumber(LoansUtilities.setLoanNumber())
                .loanType(LoanType.PERSONAL)
                .mobileNumber(LoansUtilities.setContactNumber())
                .idNumber(LoansUtilities.setIdNumber())
                .loanAmount(new BigDecimal("1000.00"))
                .outstandingBalance(new BigDecimal("1000.00"))
                .monthlyInstalment(new BigDecimal("100.00"))
                .startDate(LocalDate.now().minusMonths(1))
                .endDate(LocalDate.now().plusMonths(11))
                .status(LoanStatus.ACTIVE)
                .activeSw(true)
                .build();
    }

    @Test
    @DisplayName("1. should return transactions by id number and date range")
    void shouldReturnTransactionsByIdNumberAndDateRange() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "date";
        String sortDir = "asc";
        LocalDate from = LocalDate.now().minusDays(5);
        LocalDate to = LocalDate.now();

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        LoanTransactions tx = LoanTransactions.create(loan, new BigDecimal("10.00"), Categories.DEBIT, "Ref");
        Page<LoanTransactions> page = new PageImpl<>(List.of(tx), pageable, 1);

        when(transactionRepository.findByLoanIdNumberAndDateBetween(LoansUtilities.setIdNumber(), pageable, from, to))
                .thenReturn(page);

        LoanTransactionResponse response = underTestService.findAllByIdNumber(
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                LoansUtilities.setIdNumber(),
                from,
                to);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        verify(transactionRepository).findByLoanIdNumberAndDateBetween(LoansUtilities.setIdNumber(), pageable, from, to);
    }

    @Test
    @DisplayName("2. should credit loan on CREDIT transaction")
    void shouldCreditLoanOnCreditTransaction() {
        LoanTransactionDto dto = LoanTransactionDto.builder()
                .loanNumber(loan.getLoanNumber())
                .amount(new BigDecimal("250.00"))
                .category(Categories.CREDIT)
                .reference("Salary")
                .build();

        when(loanService.findLoan(loan.getLoanNumber())).thenReturn(loan);
        when(loanService.saveLoan(loan)).thenReturn(new ResponseDto("200", "OK"));

        underTestService.transact(dto);

        verify(transactionRepository).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue().getCategory()).isEqualTo(Categories.CREDIT);
        assertThat(loan.getLoanAmount()).isEqualByComparingTo("1250.00");
    }

    @Test
    @DisplayName("3. should debit loan on DEBIT transaction")
    void shouldDebitLoanOnDebitTransaction() {
        LoanTransactionDto dto = LoanTransactionDto.builder()
                .loanNumber(loan.getLoanNumber())
                .amount(new BigDecimal("100.00"))
                .category(Categories.DEBIT)
                .reference("Repayment")
                .build();

        when(loanService.findLoan(loan.getLoanNumber())).thenReturn(loan);
        when(loanService.saveLoan(loan)).thenReturn(new ResponseDto("200", "OK"));

        underTestService.transact(dto);

        verify(transactionRepository).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue().getCategory()).isEqualTo(Categories.DEBIT);
        assertThat(loan.getLoanAmount()).isEqualByComparingTo("900.00");
    }

    @Test
    @DisplayName("4. should throw on debit with insufficient funds")
    void shouldThrowOnDebitWithInsufficientFunds() {
        LoanTransactionDto dto = LoanTransactionDto.builder()
                .loanNumber(loan.getLoanNumber())
                .amount(new BigDecimal("5000.00"))
                .category(Categories.DEBIT)
                .reference("Overspend")
                .build();

        when(loanService.findLoan(loan.getLoanNumber())).thenReturn(loan);

        assertThatThrownBy(() -> underTestService.transact(dto))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessageContaining("Insufficient funds");

        verify(transactionRepository, never()).save(any());
    }
}

