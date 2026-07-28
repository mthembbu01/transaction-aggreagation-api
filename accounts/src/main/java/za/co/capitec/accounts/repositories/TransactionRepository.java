package za.co.capitec.accounts.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import za.co.capitec.accounts.entity.AccountsTransactions;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<AccountsTransactions,Long> {
    Page<AccountsTransactions> findByAccountsIdNumberAndDateBetween(Pageable pageable,
                                                                    String idNumber,
                                                                    LocalDate dateFrom,
                                                                    LocalDate dateTo);

    List<AccountsTransactions> findAllByAccountsAccountNumber(Long accountNumber);
}
