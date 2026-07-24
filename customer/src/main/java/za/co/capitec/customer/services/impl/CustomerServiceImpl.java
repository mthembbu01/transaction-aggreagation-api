package za.co.capitec.customer.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.capitec.customer.constants.CustomerConstants;
import za.co.capitec.customer.entity.Customers;
import za.co.capitec.customer.entity.dtos.records.CustomersRecord;
import za.co.capitec.customer.entity.dtos.requests.CreateCustomerRequest;
import za.co.capitec.customer.entity.dtos.requests.UpdateCustomerRequest;
import za.co.capitec.customer.entity.dtos.response.ResponseDto;
import za.co.capitec.customer.exceptions.ResourceAlreadyExistsException;
import za.co.capitec.customer.exceptions.ResourceNotFoundException;
import za.co.capitec.customer.repositories.CustomerRepository;
import za.co.capitec.customer.services.ICustomerService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {


    private final CustomerRepository customerRepository;

    @Override
    public CustomersRecord findByIdNumber(String idNumber) {
        //-- 1. Find the customer by ID number
        Customers customer = findCustomer(idNumber);
        //-- 2. Return the Customer Record
        return toRecord(customer);
    }

    @Override
    public List<CustomersRecord> findCustomersByIdNumber(String idNumber) {
        //-- does the customer exist by idNumber
        if (!customerRepository.existsByIdNumber(idNumber))
            throw new ResourceNotFoundException("Customer", "ID Number", idNumber);
        //-- 1. Find the customer by ID number
        return customerRepository.findAllByIdNumber(idNumber).stream()
                .filter(Customers::isActiveSw)
                .map(this::toRecord)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseDto createCustomer(CreateCustomerRequest createCustomerRequest) {
        //-- 1. Check if customer with unique attributes already exists
        isExist(createCustomerRequest.idNumber(), createCustomerRequest.mobileNumber(), createCustomerRequest.email());
        //-- 2. Build and save the new customer
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
        //-- 3. Return the response
        return new ResponseDto(CustomerConstants.STATUS_201, CustomerConstants.MESSAGE_201);
    }

    @Override
    public ResponseDto updateCustomerByIdNumber(String idNumber, UpdateCustomerRequest updateCustomerRequest) {
        //-- 1. Find the customer by ID number
        Customers customer = findCustomer(idNumber);
        //-- 2. Extract update values
        String firstName = updateCustomerRequest.firstname();
        String lastName = updateCustomerRequest.lastName();
        String address = updateCustomerRequest.address();
        String email = updateCustomerRequest.email();
        String mobileNumber = updateCustomerRequest.mobileNumber();
        String updatedIdNumber = updateCustomerRequest.idNumber();
        //-- 3. Validate unique fields
        isExist(updatedIdNumber, mobileNumber, email);
        //-- 4. Update customer attributes
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setAddress(address);
        customer.setEmail(email);
        customer.setMobileNumber(mobileNumber);
        customer.setIdNumber(updatedIdNumber);
        customerRepository.save(customer);
        //-- 5. return a proper message
        return new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200);
    }

    @Override
    public ResponseDto deleteCustomerByIdNumber(String idNumber) {
        //-- 1. Find the customer by ID number
        Customers customer = findCustomer(idNumber);
        //-- 2. Soft delete
        customer.setActiveSw(false);
        customerRepository.save(customer);
        //-- 3. return a proper message
        return new ResponseDto(CustomerConstants.STATUS_204, CustomerConstants.MESSAGE_204);
    }

    private void isExist(String idNumber, String mobileNumber, String email) {
        if (customerRepository.existsByIdNumber(idNumber)) {
            throw new ResourceAlreadyExistsException("Customer", "ID Number", idNumber);
        }
        if (customerRepository.existsByMobileNumber(mobileNumber)) {
            throw new ResourceAlreadyExistsException("Customer", "Mobile Number", mobileNumber);
        }
        if (customerRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Customer", "Email", email);
        }
    }

    private Customers findCustomer(String idNumber) {
        return customerRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "ID Number", idNumber));
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

