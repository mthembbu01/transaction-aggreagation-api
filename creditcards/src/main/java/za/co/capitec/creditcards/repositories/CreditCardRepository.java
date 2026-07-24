package za.co.capitec.creditcards.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.capitec.creditcards.entity.CreditCards;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CreditCards entities.
 */
@Repository
public interface CreditCardRepository extends JpaRepository<CreditCards, Long> {

    Optional<CreditCards> findByCardNumber(Long cardNumber);

    List<CreditCards> findAllByIdNumber(String idNumber);

    Page<CreditCards> findAllByIdNumber(String idNumber, Pageable pageable);

    Optional<CreditCards> findByMobileNumber(String mobileNumber);
}
