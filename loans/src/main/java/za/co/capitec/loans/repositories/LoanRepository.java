package za.co.capitec.loans.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.capitec.loans.entity.Loans;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Loans entities.
 */
@Repository
public interface LoanRepository extends JpaRepository<Loans, Long> {

    Optional<Loans> findByLoanNumber(Long loanNumber);

    List<Loans> findAllByIdNumber(String idNumber);

    Page<Loans> findAllByIdNumber(String idNumber, Pageable pageable);

    Optional<Loans> findByMobileNumber(String mobileNumber);
}
