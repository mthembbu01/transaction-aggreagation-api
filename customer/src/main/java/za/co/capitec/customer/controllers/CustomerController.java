package za.co.capitec.customer.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.customer.entity.dtos.records.CustomersRecord;
import za.co.capitec.customer.entity.dtos.requests.CreateCustomerRequest;
import za.co.capitec.customer.entity.dtos.requests.UpdateCustomerRequest;
import za.co.capitec.customer.entity.dtos.response.CustomerResponse;


import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1")
public class CustomerController {

//    private final ICustomerService service;

    //-- http://localhost:8080/api/v11?pageNo=1&pageSize=10&sortBy=accountType&sortDir=asc
    @GetMapping
    public ResponseEntity<CustomerResponse> getAllCustomers(@RequestParam(defaultValue = "0") int pageNo,
                                                            @RequestParam(defaultValue = "10") int pageSize,
                                                            @RequestParam(defaultValue = "accountType") String sortBy,
                                                            @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Received find all Customers request");
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
    //-- http://localhost:8080/api/v1/{idNumber}
    @GetMapping(path = "/{idNumber}")
    public ResponseEntity<CustomersRecord> handleFindById(@PathVariable("idNumber") String idNumber) {
        log.info("Received find Customer by ID Number: {}", idNumber);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/customer
    @PostMapping
    public ResponseEntity<Object> handleCreate(@RequestBody CreateCustomerRequest createCustomerRequest) {
        log.info("Received create new customer request {}", createCustomerRequest);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
    //-- http://localhost:8080/api/v1/customer/{idNumber}
    @PutMapping(path = "/{idNumber}")
    public ResponseEntity<Object> handleUpdate(@PathVariable("idNumber") String idNumber,@RequestBody UpdateCustomerRequest updateCustomerRequest) {
        log.info("Received the update Customer request with ID number: {}", idNumber);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @DeleteMapping(path = "/{idNumber}")
    public void handleDelete(@PathVariable("idNumber") String idNumber) {
        log.info("Received the delete request of idNumber: {}", idNumber);
    }
}
