package za.co.capitec.accounts.controllers;


import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.accounts.services.IAccountService;
import za.co.capitec.coreapi.dtos.accounts.records.AccountsRecord;
import za.co.capitec.coreapi.dtos.accounts.requests.CreateAccountsDto;
import za.co.capitec.coreapi.dtos.accounts.requests.UpdateAccountDto;
import za.co.capitec.coreapi.dtos.ResponseDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class AccountsController {

    private final IAccountService service;

    /**
     * Find Account number by accountNumber
     * @param accountNumber
     * @return
     */
    //-- http://localhost:8080/api/v1/{accountNumber}
    @GetMapping(path = "/{accountNumber}")
    public ResponseEntity<AccountsRecord> handleFindByAccNumber(@PathVariable("accountNumber") Long accountNumber) {
        log.info("Received find account by id request for accountNumber: {}", accountNumber);
        return new ResponseEntity<>(service.findByAccNumber(accountNumber), HttpStatus.OK);
    }
    /**
     *
     * @param idNumber
     * @return
     */
    //-- http://localhost:8080/api/v1/accounts?idNumber=123456
    @GetMapping(path = "/accounts")
    public ResponseEntity<List<AccountsRecord>> handleFindByIDNumber(@RequestParam("idNumber") @Pattern(regexp = "\\d{13}") String idNumber) {
        log.info("Received find accounts by ID Number: {}", idNumber);
        return new ResponseEntity<>(service.findAccountsByIdNumber(idNumber), HttpStatus.OK);
    }
    /**
     *
     * @param createAccountsDto
     * @return
     */
    //-- http://localhost:8080/api/v1/
    @PostMapping
    public ResponseEntity<ResponseDto> handleCreate(@RequestBody CreateAccountsDto createAccountsDto) {
        log.info("Received create new account request {}", createAccountsDto);
        return new ResponseEntity<>(service.createAccount(createAccountsDto), HttpStatus.CREATED);
    }
    //-- http://localhost:8080/api/v1/{1}
    @PutMapping(path = "/{accountNumber}")
    public ResponseEntity<ResponseDto> handleUpdate(@PathVariable("accountNumber") Long accountNumber, @RequestBody UpdateAccountDto updateAccountDto) {
        log.info("Received the update request of accountNumber: {}",accountNumber);
        return new ResponseEntity<>(service.updateAccountByAccNumber(accountNumber, updateAccountDto), HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/{1}
    @PatchMapping(path = "/{accountNumber}")
    public ResponseEntity<ResponseDto> handlePatch(@PathVariable("accountNumber") Long accountNumber, @RequestBody UpdateAccountDto updateAccountDto) {
        log.info("Received the patch request of accountNumber: {}",accountNumber);
        return new ResponseEntity<>(service.updateAccountByAccNumber(accountNumber, updateAccountDto),HttpStatus.OK);
    }

    @DeleteMapping(path = "/{accountNumber}")
    public ResponseEntity<ResponseDto> handleDelete(@PathVariable("accountNumber") Long accountNumber) {
        log.info("Received the delete request of accountNumber: {}", accountNumber);
        return new ResponseEntity<>(service.deleteAccountByAccNumber(accountNumber), HttpStatus.NO_CONTENT);
    }
}
