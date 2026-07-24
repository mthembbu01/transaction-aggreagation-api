package za.co.capitec.accounts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.capitec.accounts.entity.Accounts;

import java.util.List;
import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Accounts,Long> {
    Optional<Accounts> findByAccountNumber(Long accountNumber);
    List<Accounts> findAllByIdNumber(String idNumber);

    boolean existsByIdNumber(String idNumber);
    boolean existsByMobileNumber(String mobileNumber);
}