package za.co.capitec.customer.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.customer.entity.dtos.records.CustomersRecord;
import za.co.capitec.customer.entity.dtos.requests.CreateCustomerRequest;
import za.co.capitec.customer.entity.dtos.requests.UpdateCustomerRequest;
import za.co.capitec.customer.entity.dtos.response.CustomerResponse;
import za.co.capitec.customer.entity.dtos.response.ResponseDto;
import za.co.capitec.customer.services.ICustomerService;



@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class CustomerController {

    private final ICustomerService service;

    //-- http://localhost:8080/api/v11?pageNo=1&pageSize=10&sortBy=accountType&sortDir=asc
    @GetMapping
    public ResponseEntity<CustomerResponse> getAllCustomers(@RequestParam(defaultValue = "0") int pageNo,
                                                            @RequestParam(defaultValue = "10") int pageSize,
                                                            @RequestParam(defaultValue = "firstName") String sortBy,
                                                            @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Received find all Customers request");
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return new ResponseEntity<>(service.findAll(pageable), HttpStatus.OK);
    }
    //-- http://localhost:8080/api/v1/{idNumber}
    @GetMapping(path = "/{idNumber}")
    public ResponseEntity<CustomersRecord> handleFindById(@PathVariable("idNumber") String idNumber) {
        log.info("Received find Customer by ID Number: {}", idNumber);
        return new ResponseEntity<>(service.findByIdNumber(idNumber), HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/customer
    @PostMapping
    public ResponseEntity<ResponseDto> handleCreate(@RequestBody CreateCustomerRequest createCustomerRequest) {
        log.info("Received create new customer request {}", createCustomerRequest);
        return new ResponseEntity<>(service.createCustomer(createCustomerRequest), HttpStatus.CREATED);
    }
    //-- http://localhost:8080/api/v1/customer/{idNumber}
    @PutMapping(path = "/{idNumber}")
    public ResponseEntity<ResponseDto> handleUpdate(@PathVariable("idNumber") String idNumber,@RequestBody UpdateCustomerRequest updateCustomerRequest) {
        log.info("Received the update Customer request with ID number: {}", idNumber);
        return new ResponseEntity<>(service.updateCustomerByIdNumber(idNumber, updateCustomerRequest), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{idNumber}")
    public ResponseEntity<ResponseDto> handleDelete(@PathVariable("idNumber") String idNumber) {
        log.info("Received the delete request of idNumber: {}", idNumber);
        return new ResponseEntity<>(service.deleteCustomerByIdNumber(idNumber), HttpStatus.OK);
    }
}
