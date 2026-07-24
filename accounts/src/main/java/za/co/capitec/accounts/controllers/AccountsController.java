package za.co.capitec.accounts.controllers;


import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.accounts.dtos.records.AccountsRecord;
import za.co.capitec.accounts.dtos.requests.CreateAccountsDto;
import za.co.capitec.accounts.dtos.requests.UpdateAccountDto;
import za.co.capitec.accounts.dtos.response.AccountsResponse;
import za.co.capitec.accounts.services.IAccountService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class AccountsController {

    private final IAccountService service;

    /**
     *
     */
    //-- http://localhost:8080/api/v1/{accountNumber}
    @GetMapping(path = "/{accountNumber}")
    public ResponseEntity<AccountsRecord> handleFindById(@PathVariable("accountNumber") Long accountNumber) {
        log.info("Received find account by id request for accountNumber: {}", accountNumber);
        return new ResponseEntity<>(service.findByAccNumber(accountNumber), HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/search?idNumber=123456
    @GetMapping(path = "/accounts")
    public ResponseEntity<List<AccountsRecord>> handleFindByIDNumber(@RequestParam("idNumber") @Pattern(regexp = "\\d{13}") String idNumber) {
        log.info("Received find accounts by ID Number: {}", idNumber);
        return new ResponseEntity<>(service.findAccountsByIdNumber(idNumber), HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/
    @PostMapping
    public ResponseEntity<Object> handleCreate(@RequestBody CreateAccountsDto createAccountsDto) {
        log.info("Received create new account request {}", createAccountsDto);
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
