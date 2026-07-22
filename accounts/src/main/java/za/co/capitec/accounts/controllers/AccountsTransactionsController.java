package za.co.capitec.accounts.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.accounts.dtos.requests.TransactionDto;
import za.co.capitec.accounts.dtos.requests.TransferDto;
import za.co.capitec.accounts.dtos.response.AccountTransaction;
import za.co.capitec.accounts.dtos.response.TransactionResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@Slf4j
public class AccountsTransactionsController {

//    private final IAccountTransaction service;
//
//    public AccountsTransactionsController(final IAccountTransaction service) {
//        this.service = service;
//    }
    //-- save a single account transfer transaction api
    //-- http://localhost:8090/api/v1/transfer
    /**
     * @param transferDto
     * @return
     */
    @PostMapping("/transfer")
    public ResponseEntity<String> handleTransfer(@RequestBody TransferDto transferDto) {
        log.info("Transfer request received: {}", transferDto);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    //-- save a single account CREDIT/DEBIT transaction api
    //-- http://localhost:8090/api/v1/transaction
    @PostMapping
    public ResponseEntity<String> handleTransactions(@RequestBody TransactionDto transactionDto) {
        log.info("Transaction request received: {}", transactionDto);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    //-- Typical Bank Statement
    //-- get all transactions using the account number between startDate and endDate api
    //-- http://localhost:8091/api/v1/{accountNumber}/{startDate}/{endDate}?pageNo=0&pageSize=10&sortBy=date&sortDir=asc
    /**
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @param accountNumber
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/{accountNumber}/{startDate}/{endDate}")
    public ResponseEntity<TransactionResponse> handleFindAll(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                             @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                             @RequestParam(value = "sortBy", defaultValue = "Date", required = false) String sortBy,
                                                             @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir,
                                                             @PathVariable("accountNumber") Long accountNumber,
                                                             @PathVariable("startDate") LocalDate startDate,
                                                             @PathVariable("endDate") LocalDate endDate) {
        log.info("Find all transactions for accountNumber: {} from {} to {}", accountNumber, startDate, endDate);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    //-- Typical Bank Statement
    //-- get all transactions using the idNumber account number between startDate and endDate api
    //-- http://localhost:8090/api/v1/transaction/{idNumber}?startDate={startDate}&endDate={endDate}&pageNo=0&pageSize=10&sortBy=date&sortDir=asc
    /**
     *
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @param idNumber
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/{idNumber}")
    public ResponseEntity<List<AccountTransaction>> handleFindAll(@PathVariable("idNumber") Long idNumber,
                                                                 @RequestParam("startDate") LocalDate startDate,
                                                                 @RequestParam("endDate") LocalDate endDate,
                                                                 @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                 @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                 @RequestParam(value = "sortBy", defaultValue = "date", required = false) String sortBy,
                                                                 @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        //TODO: For each account from the idNumber, fetch transactions, build a TransactionResponse, then convert it into a list
        log.info("Find all accounts transactions for idNumber: {}, from:{} to: {}", idNumber,startDate,endDate);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
