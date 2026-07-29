package za.co.capitec.customer.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import za.co.capitec.coreapi.dtos.ResponseDto;
import za.co.capitec.coreapi.dtos.customer.records.CustomersRecord;
import za.co.capitec.coreapi.dtos.customer.requests.CreateCustomerRequest;
import za.co.capitec.coreapi.dtos.customer.requests.UpdateCustomerRequest;
import za.co.capitec.coreapi.dtos.customer.response.CustomerResponse;
import za.co.capitec.customer.constants.CustomerConstants;
import za.co.capitec.customer.entity.Customers;
import za.co.capitec.customer.exceptions.ResourceAlreadyExistsException;
import za.co.capitec.customer.exceptions.ResourceNotFoundException;
import za.co.capitec.customer.repositories.CustomerRepository;
import za.co.capitec.customer.services.ICustomerService;
import za.co.capitec.customer.utilities.dates.CustomerUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {


    private final CustomerRepository customerRepository;

    /**
     *
     * @param idNumber
     * @return
     */
    @Override
    public CustomersRecord findByIdNumber(String idNumber) {
        //-- 1. Find the customer by ID number
        Customers customer = findCustomer(idNumber);
        //-- 2. Return the Customer Record
        return CustomerUtils.toRecord(customer);
    }
    /**
     *
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @Override
    public CustomerResponse findAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        //-- conditional sort object declaration
        Sort sort = sortDirection(sortBy, sortDir);
        //-- 1. create a pageable instance. Add sortDirection: Ascending or Descending order
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        //-- 2. Find all Customers using the Pageable above
        Page<Customers> page = customerRepository.findAll(pageable);
        //-- 3. Return the Customer Response for pagination
        return CustomerUtils.toCustomerResponse(page);
    }
    /**
     *
     * @param createCustomerRequest
     * @return
     */
    @Override
    public ResponseDto createCustomer(CreateCustomerRequest createCustomerRequest) {
        //-- 1. Check if customer with unique attributes already exists
        isExist(createCustomerRequest.getIdNumber(), createCustomerRequest.getMobileNumber(), createCustomerRequest.getEmail());
        //-- 2. Build and save the new customer
        Customers customer = Customers.builder()
                .firstName(createCustomerRequest.getFirstname())
                .lastName(createCustomerRequest.getLastName())
                .address(createCustomerRequest.getAddress())
                .email(createCustomerRequest.getEmail())
                .mobileNumber(createCustomerRequest.getMobileNumber())
                .idNumber(createCustomerRequest.getIdNumber())
                .activeSw(true)
                .build();
        customerRepository.save(customer);
        //-- 3. Return the response
        return new ResponseDto(CustomerConstants.STATUS_201, CustomerConstants.MESSAGE_201);
    }
    /**
     *
     * @param idNumber
     * @param updateCustomerRequest
     * @return
     */
    @Override
    public ResponseDto updateCustomerByIdNumber(String idNumber, UpdateCustomerRequest updateCustomerRequest) {
        //-- 1. Find the customer by ID number
        Customers customer = findCustomer(idNumber);
        //-- 2. Extract update values
        String firstName = updateCustomerRequest.getFirstname();
        String lastName = updateCustomerRequest.getLastName();
        String address = updateCustomerRequest.getAddress();
        String email = updateCustomerRequest.getEmail();
        String mobileNumber = updateCustomerRequest.getMobileNumber();
        String updatedIdNumber = updateCustomerRequest.getIdNumber();
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
    /**
     *
     * @param idNumber
     * @return
     */
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
    /**
     *
     * @param idNumber
     * @return
     */
    private Customers findCustomer(String idNumber) {
        return customerRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "ID Number", idNumber));
    }
    /**
     *
     * @param idNumber
     * @param mobileNumber
     * @param email
     */
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
    /**
     * @param sortBy ascending or descending order
     * @param sortDir
     * @return
     */
    private Sort sortDirection(String sortBy, String sortDir) {
        return sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
    }
}

