package za.co.capitec.creditcards.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.capitec.creditcards.entity.CreditCardTransactions;

import java.time.LocalDate;

@Repository
public interface CreditCardTransactionRepository extends JpaRepository<CreditCardTransactions, Long> {

    Page<CreditCardTransactions> findByCreditCardIdNumberAndDateBetween(String idNumber,
                                                                           Pageable pageable,
                                                                           LocalDate dateFrom,
                                                                           LocalDate dateTo);
}

