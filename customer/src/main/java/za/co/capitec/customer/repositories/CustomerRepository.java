package za.co.capitec.customer.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.capitec.customer.entity.Customers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Customer entities.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customers, Long> {

    Optional<Customers> findByIdNumber(String idNumber);

    Optional<Customers> findByEmail(String email);

    Optional<Customers> findByMobileNumber(String mobileNumber);

    boolean existsByIdNumber(String idNumber);

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByEmail(String email);

    List<Customers> findAllByIdNumber(String idNumber);
}
