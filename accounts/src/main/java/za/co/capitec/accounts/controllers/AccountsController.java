package za.co.capitec.accounts.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.accounts.dtos.requests.CreateAccountsDto;
import za.co.capitec.accounts.dtos.requests.UpdateAccountDto;
import za.co.capitec.accounts.dtos.response.AccountsResponse;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1")
public class AccountsController {

//    private final IAccountService service;
//
//    public AccountsController(IAccountService service) {
//        this.service = service;
//    }
    //-- http://localhost:8080/api/v1?pageNo=1&pageSize=10&sortBy=accountType&sortDir=asc
    @GetMapping
    public ResponseEntity<AccountsResponse> handleFindAll(@RequestParam(defaultValue = "0") int pageNo,
                                                          @RequestParam(defaultValue = "10") int pageSize,
                                                          @RequestParam(defaultValue = "accountType") String sortBy,
                                                          @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Received find all accounts request");
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/{accountNumber}
    @GetMapping(path = "/{accountNumber}")
    public ResponseEntity<Object> handleFindById(@PathVariable("accountNumber") Long accountNumber) {
        log.info("Received find account by id request for accountNumber: {}", accountNumber);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/search?idNumber=123456&mobileNumber=0788298725
    @GetMapping(path = "/search")
    public ResponseEntity<Object> handleFindBySearchCriteria(@RequestParam("idNumber") String idNumber, @RequestParam("mobileNumber") String mobileNumber) {
        log.info("Received find account by records criteria request for idNumber: {} and mobileNumber: {}", idNumber, mobileNumber);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/
    @PostMapping
    public ResponseEntity<Object> handleCreate(@RequestBody CreateAccountsDto createAccountsDto) {
        log.info("Received create new applicant {}", createAccountsDto);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
    //-- http://localhost:8080/api/v1/1
    @PutMapping(path = "/{accountNumber}")
    public ResponseEntity<Object> handleUpdate(@PathVariable("accountNumber") Long accountNumber, @RequestBody UpdateAccountDto updateAccountDto) {
        log.info("Received the update request of accountNumber: {}",accountNumber);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/1
    @PatchMapping(path = "/{accountNumber}")
    public ResponseEntity<Object> handlePatch(@PathVariable("accountNumber") Long accountNumber, @RequestBody UpdateAccountDto updateAccountDto) {
        log.info("Received the patch request of accountNumber: {}",accountNumber);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @DeleteMapping(path = "/{accountNumber}")
    public void handleDelete(@PathVariable("accountNumber") Long accountNumber) {
        log.info("Received the delete request of accountNumber: {}", accountNumber);
    }
}
