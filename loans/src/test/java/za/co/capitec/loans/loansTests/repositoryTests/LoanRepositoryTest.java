package za.co.capitec.loans.loansTests.repositoryTests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import za.co.capitec.loans.Abstracts.AbstractContainersTest;
import za.co.capitec.loans.entity.LoanTransactions;
import za.co.capitec.loans.entity.Loans;
import za.co.capitec.loans.enums.Categories;
import za.co.capitec.loans.enums.LoanStatus;
import za.co.capitec.loans.enums.LoanType;
import za.co.capitec.loans.repositories.LoanRepository;
import za.co.capitec.loans.repositories.LoanTransactionRepository;
import za.co.capitec.loans.utils.LoansUtilities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LoanRepositoryTest extends AbstractContainersTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanTransactionRepository loanTransactionRepository;

    private Loans savedLoan;
    private Long loanNumber;
    private String idNumber;

    @BeforeEach
    void setUp() {
        loanNumber = LoansUtilities.setLoanNumber();
        idNumber = LoansUtilities.setIdNumber();

        Loans loan = Loans.builder()
                .loanNumber(loanNumber)
                .loanType(LoanType.PERSONAL)
                .mobileNumber(LoansUtilities.setContactNumber())
                .idNumber(idNumber)
                .loanAmount(new BigDecimal("2000.00"))
                .outstandingBalance(new BigDecimal("2000.00"))
                .monthlyInstalment(new BigDecimal("500.00"))
                .startDate(LocalDate.now().minusMonths(1))
                .endDate(LocalDate.now().plusMonths(3))
                .status(LoanStatus.ACTIVE)
                .activeSw(true)
                .build();

        savedLoan = loanRepository.save(loan);

        LoanTransactions transaction = LoanTransactions.builder()
                .loan(savedLoan)
                .category(Categories.DEBIT)
                .amount(new BigDecimal("100.00"))
                .reference("Loan test transaction")
                .date(LocalDate.now())
                .build();

        loanTransactionRepository.save(transaction);
        log.info("Saved loan id={}, loanNumber={}", savedLoan.getId(), savedLoan.getLoanNumber());
    }

    @AfterEach
    void tearDown() {
        loanTransactionRepository.deleteAll();
        loanRepository.deleteAll();
    }

    @Test
    @DisplayName("1. should return loan when find by loan number")
    void shouldReturnLoanWhenFindByLoanNumber() {
        Optional<Loans> reloaded = loanRepository.findByLoanNumber(loanNumber);
        assertThat(reloaded).isPresent();
    }

    @Test
    @DisplayName("2. should return loans when find by id number")
    void shouldReturnLoansWhenFindByIdNumber() {
        List<Loans> reloaded = loanRepository.findAllByIdNumber(idNumber);
        assertThat(reloaded).isNotEmpty();
    }

    @Test
    @DisplayName("3. should not return loan when find by invalid loan number")
    void shouldNotReturnLoanWhenFindByInvalidLoanNumber() {
        Optional<Loans> reloaded = loanRepository.findByLoanNumber(LoansUtilities.updatedLoanNumber());
        assertThat(reloaded).isNotPresent();
    }

    @Test
    @DisplayName("4. should not return loans when find by invalid id number")
    void shouldNotReturnLoansWhenFindByInvalidIdNumber() {
        List<Loans> reloaded = loanRepository.findAllByIdNumber(LoansUtilities.updatedIdNumber());
        assertThat(reloaded).isEmpty();
    }

    @Test
    @DisplayName("5. should return loan transactions by id number and date range")
    void shouldReturnLoanTransactionsByIdNumberAndDateRange() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate today = LocalDate.now();

        Page<LoanTransactions> reloaded = loanTransactionRepository.findByLoanIdNumberAndDateBetween(
                idNumber,
                pageable,
                today.minusDays(1),
                today.plusDays(1));

        assertThat(reloaded.getContent()).isNotEmpty();
        assertThat(reloaded.getContent())
                .extracting(tx -> tx.getLoan().getIdNumber())
                .containsOnly(idNumber);
    }
}

