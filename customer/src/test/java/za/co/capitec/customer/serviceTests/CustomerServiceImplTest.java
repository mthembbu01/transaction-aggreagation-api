package za.co.capitec.customer.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.capitec.coreapi.dtos.ResponseDto;
import za.co.capitec.coreapi.dtos.customer.records.CustomersRecord;
import za.co.capitec.coreapi.dtos.customer.requests.CreateCustomerRequest;
import za.co.capitec.coreapi.dtos.customer.requests.UpdateCustomerRequest;
import za.co.capitec.coreapi.dtos.customer.response.CustomerResponse;
import za.co.capitec.customer.entity.Customers;
import za.co.capitec.customer.exceptions.ResourceAlreadyExistsException;
import za.co.capitec.customer.exceptions.ResourceNotFoundException;
import za.co.capitec.customer.repositories.CustomerRepository;
import za.co.capitec.customer.services.impl.CustomerServiceImpl;
import za.co.capitec.customer.utils.CustomerUtilities;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    private CustomerServiceImpl underTestService;

    @Mock
    private CustomerRepository customerRepository;

    @Captor
    private ArgumentCaptor<Customers> customersCaptor;

    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    private Customers activeCustomer;
    private CreateCustomerRequest createCustomerRequest;

    @BeforeEach
    void setUp() {
        underTestService = new CustomerServiceImpl(customerRepository);

        activeCustomer = Customers.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .mobileNumber(CustomerUtilities.setContactNumber())
                .idNumber(CustomerUtilities.setIdNumber())
                .email(CustomerUtilities.setEmail())
                .address("129 Botanic Gardens")
                .activeSw(true)
                .build();

        createCustomerRequest = new CreateCustomerRequest(
                "John",
                "Doe",
                "129 Botanic Gardens",
                CustomerUtilities.setEmail(),
                CustomerUtilities.setContactNumber(),
                CustomerUtilities.setIdNumber());
    }

    @Test
    @DisplayName("1. should return customer record when find by id number")
    void shouldReturnCustomerRecordWhenFindByIdNumber() {
        when(customerRepository.findByIdNumber(CustomerUtilities.setIdNumber()))
                .thenReturn(Optional.of(activeCustomer));

        CustomersRecord record = underTestService.findByIdNumber(CustomerUtilities.setIdNumber());

        assertThat(record.getIdNumber()).isEqualTo(CustomerUtilities.setIdNumber());
        assertThat(record.getEmail()).isEqualTo(CustomerUtilities.setEmail());
        verify(customerRepository).findByIdNumber(CustomerUtilities.setIdNumber());
    }

    @Test
    @DisplayName("2. should return paginated customers when find all")
    void shouldReturnPaginatedCustomersWhenFindAll() {
        Page<Customers> customerPage = new PageImpl<>(
                List.of(activeCustomer),
                PageRequest.of(0, 10, Sort.by("id").ascending()),
                1
        );

        when(customerRepository.findAll(any(Pageable.class))).thenReturn(customerPage);

        CustomerResponse response = underTestService.findAll(0, 10, "id", "asc");

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().getFirst().isActiveSw()).isTrue();
        verify(customerRepository).findAll(pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(0);
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort().getOrderFor("id")).isNotNull();
        assertThat(pageable.getSort().getOrderFor("id").getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    @DisplayName("3. should create customer when unique fields do not exist")
    void shouldCreateCustomerWhenUniqueFieldsDoNotExist() {
        ResponseDto response = underTestService.createCustomer(createCustomerRequest);

        verify(customerRepository).save(customersCaptor.capture());
        Customers captured = customersCaptor.getValue();
        assertThat(captured.getIdNumber()).isEqualTo(createCustomerRequest.getIdNumber());
        assertThat(captured.getMobileNumber()).isEqualTo(createCustomerRequest.getMobileNumber());
        assertThat(captured.getEmail()).isEqualTo(createCustomerRequest.getEmail());
        assertThat(captured.isActiveSw()).isTrue();
        assertThat(response.getStatusCode()).isEqualTo("201");
    }

    @Test
    @DisplayName("4. should update customer by id number")
    void shouldUpdateCustomerByIdNumber() {
        UpdateCustomerRequest updateRequest = new UpdateCustomerRequest(
                "Johnny",
                "Doel",
                "130 Botanic Gardens",
                CustomerUtilities.updatedEmail(),
                CustomerUtilities.updatedContactNumber(),
                CustomerUtilities.updatedIdNumber());

        when(customerRepository.findByIdNumber(CustomerUtilities.setIdNumber())).thenReturn(Optional.of(activeCustomer));

        ResponseDto response = underTestService.updateCustomerByIdNumber(CustomerUtilities.setIdNumber(), updateRequest);

        verify(customerRepository).save(customersCaptor.capture());
        Customers updated = customersCaptor.getValue();
        assertThat(updated.getFirstName()).isEqualTo("Johnny");
        assertThat(updated.getIdNumber()).isEqualTo(CustomerUtilities.updatedIdNumber());
        assertThat(response.getStatusCode()).isEqualTo("200");
    }

    @Test
    @DisplayName("5. should soft delete customer by id number")
    void shouldSoftDeleteCustomerByIdNumber() {
        when(customerRepository.findByIdNumber(CustomerUtilities.setIdNumber())).thenReturn(Optional.of(activeCustomer));

        ResponseDto response = underTestService.deleteCustomerByIdNumber(CustomerUtilities.setIdNumber());

        verify(customerRepository).save(customersCaptor.capture());
        assertThat(customersCaptor.getValue().isActiveSw()).isFalse();
        assertThat(response.getStatusCode()).isEqualTo("204");
    }

    @Test
    @DisplayName("6. should throw not found when customer id does not exist")
    void shouldThrowNotFoundWhenCustomerIdDoesNotExist() {
        when(customerRepository.findByIdNumber(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTestService.findByIdNumber(CustomerUtilities.updatedIdNumber()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ID Number");
    }

    @Test
    @DisplayName("7. should throw already exists when id number exists")
    void shouldThrowAlreadyExistsWhenIdNumberExists() {
        when(customerRepository.existsByIdNumber(createCustomerRequest.getIdNumber())).thenReturn(true);

        assertThatThrownBy(() -> underTestService.createCustomer(createCustomerRequest))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("ID Number");

        verify(customerRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}

