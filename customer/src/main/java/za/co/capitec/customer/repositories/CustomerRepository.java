package za.co.capitec.customer.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.capitec.customer.entity.Customers;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Customer entities.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customers, Long> {

    Optional<Customers> findByEmail(String email);

    Optional<Customers> findByMobileNumber(String mobileNumber);

    List<Customers> findAllByIdNumber(String idNumber);

    Page<Customers> findAllByIdNumber(String idNumber, Pageable pageable);
}
