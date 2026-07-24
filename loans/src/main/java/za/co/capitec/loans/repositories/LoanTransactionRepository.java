package za.co.capitec.loans.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.capitec.loans.entity.LoanTransactions;

import java.time.LocalDate;

@Repository
public interface LoanTransactionRepository extends JpaRepository<LoanTransactions, Long> {

    Page<LoanTransactions> findByLoanLoanNumberAndDateBetween(Long loanNumber,
                                                               Pageable pageable,
                                                               LocalDate dateFrom,
                                                               LocalDate dateTo);
}

