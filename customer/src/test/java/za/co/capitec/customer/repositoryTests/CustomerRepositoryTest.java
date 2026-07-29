package za.co.capitec.customer.repositoryTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import za.co.capitec.customer.Abstracts.AbstractContainersTest;
import za.co.capitec.customer.entity.Customers;
import za.co.capitec.customer.repositories.CustomerRepository;
import za.co.capitec.customer.utils.CustomerUtilities;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractContainersTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customers savedCustomer;

    @BeforeEach
    void setUp() {
        Customers customer = Customers.builder()
                .firstName("John")
                .lastName("Doe")
                .mobileNumber(CustomerUtilities.setContactNumber())
                .idNumber(CustomerUtilities.setIdNumber())
                .email(CustomerUtilities.setEmail())
                .address("129 Botanic Gardens")
                .activeSw(true)
                .build();

        savedCustomer = customerRepository.save(customer);
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("1. should return customer when find by id number")
    void shouldReturnCustomerWhenFindByIdNumber() {
        Optional<Customers> reloaded = customerRepository.findByIdNumber(savedCustomer.getIdNumber());
        assertThat(reloaded).isPresent();
    }

    @Test
    @DisplayName("2. should return customer when find by email")
    void shouldReturnCustomerWhenFindByEmail() {
        Optional<Customers> reloaded = customerRepository.findByEmail(CustomerUtilities.setEmail());
        assertThat(reloaded).isPresent();
    }

    @Test
    @DisplayName("3. should return customer when find by mobile number")
    void shouldReturnCustomerWhenFindByMobileNumber() {
        Optional<Customers> reloaded = customerRepository.findByMobileNumber(CustomerUtilities.setContactNumber());
        assertThat(reloaded).isPresent();
    }

    @Test
    @DisplayName("4. should return customers list by id number")
    void shouldReturnCustomersByIdNumber() {
        List<Customers> reloaded = customerRepository.findAllByIdNumber(CustomerUtilities.setIdNumber());
        assertThat(reloaded).isNotEmpty();
    }

    @Test
    @DisplayName("5. should return false for non-existing unique values")
    void shouldReturnFalseForNonExistingUniqueValues() {
        assertThat(customerRepository.existsByIdNumber(CustomerUtilities.updatedIdNumber())).isFalse();
        assertThat(customerRepository.existsByEmail(CustomerUtilities.updatedEmail())).isFalse();
        assertThat(customerRepository.existsByMobileNumber(CustomerUtilities.updatedContactNumber())).isFalse();
    }

    @Test
    @DisplayName("6. should return true for existing unique values")
    void shouldReturnTrueForExistingUniqueValues() {
        assertThat(savedCustomer).isNotNull();
        assertThat(customerRepository.existsByIdNumber(CustomerUtilities.setIdNumber())).isTrue();
        assertThat(customerRepository.existsByEmail(CustomerUtilities.setEmail())).isTrue();
        assertThat(customerRepository.existsByMobileNumber(CustomerUtilities.setContactNumber())).isTrue();
    }
}

