package za.co.capitec.customer.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import za.co.capitec.customer.constants.CustomerConstants;
import za.co.capitec.customer.entity.Customers;
import za.co.capitec.customer.entity.dtos.records.CustomersRecord;
import za.co.capitec.customer.entity.dtos.requests.CreateCustomerRequest;
import za.co.capitec.customer.entity.dtos.requests.UpdateCustomerRequest;
import za.co.capitec.customer.entity.dtos.response.CustomerResponse;
import za.co.capitec.customer.entity.dtos.response.ResponseDto;
import za.co.capitec.customer.exceptions.ResourceAlreadyExistsException;
import za.co.capitec.customer.exceptions.ResourceNotFoundException;
import za.co.capitec.customer.repositories.CustomerRepository;
import za.co.capitec.customer.services.ICustomerService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse findAll(Pageable pageable) {
        Page<Customers> page = customerRepository.findAll(pageable);
        List<CustomersRecord> records = page.getContent().stream()
                .map(this::toRecord)
                .toList();

        return CustomerResponse.builder()
                .content(records)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .isLast(page.isLast())
                .build();
    }

    @Override
    public CustomersRecord findByIdNumber(String idNumber) {
        Customers customer = customerRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "idNumber", idNumber));

        if (!customer.isActiveSw()) {
            throw new ResourceNotFoundException("Customer", "idNumber", idNumber);
        }

        return toRecord(customer);
    }

    @Override
    public List<CustomersRecord> findCustomersByIdNumber(String idNumber) {
        if (!customerRepository.existsByIdNumber(idNumber)) {
            throw new ResourceNotFoundException("Customer", "idNumber", idNumber);
        }

        return customerRepository.findAllByIdNumber(idNumber).stream()
                .filter(Customers::isActiveSw)
                .map(this::toRecord)
                .toList();
    }

    @Override
    public ResponseDto createCustomer(CreateCustomerRequest createCustomerRequest) {
        validateUniqueFields(createCustomerRequest.idNumber(), createCustomerRequest.mobileNumber(), createCustomerRequest.email());

        Customers customer = Customers.builder()
                .firstName(createCustomerRequest.firstname())
                .lastName(createCustomerRequest.lastName())
                .address(createCustomerRequest.address())
                .email(createCustomerRequest.email())
                .mobileNumber(createCustomerRequest.mobileNumber())
                .idNumber(createCustomerRequest.idNumber())
                .activeSw(true)
                .build();

        customerRepository.save(customer);

        return ResponseDto.builder()
                .statusCode(CustomerConstants.STATUS_201)
                .statusMsg(CustomerConstants.MESSAGE_201)
                .build();
    }

    @Override
    public ResponseDto updateCustomerByIdNumber(String idNumber, UpdateCustomerRequest updateCustomerRequest) {
        Customers customer = customerRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "idNumber", idNumber));

        customer.setFirstName(updateCustomerRequest.firstname());
        customer.setLastName(updateCustomerRequest.lastName());
        customer.setAddress(updateCustomerRequest.address());
        customer.setEmail(updateCustomerRequest.email());
        customer.setMobileNumber(updateCustomerRequest.mobileNumber());
        customer.setIdNumber(updateCustomerRequest.idNumber());

        customerRepository.save(customer);

        return ResponseDto.builder()
                .statusCode(CustomerConstants.STATUS_200)
                .statusMsg(CustomerConstants.MESSAGE_200)
                .build();
    }

    @Override
    public ResponseDto deleteCustomerByIdNumber(String idNumber) {
        Customers customer = customerRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "idNumber", idNumber));

        customer.setActiveSw(false);
        customerRepository.save(customer);

        return ResponseDto.builder()
                .statusCode(CustomerConstants.STATUS_204)
                .statusMsg(CustomerConstants.MESSAGE_204)
                .build();
    }

    private void validateUniqueFields(String idNumber, String mobileNumber, String email) {
        if (customerRepository.existsByIdNumber(idNumber)) {
            throw new ResourceAlreadyExistsException("Customer", "idNumber", idNumber);
        }
        if (customerRepository.existsByMobileNumber(mobileNumber)) {
            throw new ResourceAlreadyExistsException("Customer", "mobileNumber", mobileNumber);
        }
        if (customerRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Customer", "email", email);
        }
    }

    private CustomersRecord toRecord(Customers customer) {
        return new CustomersRecord(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getMobileNumber(),
                customer.getIdNumber(),
                customer.getEmail(),
                customer.getAddress(),
                customer.isActiveSw()
        );
    }
}

