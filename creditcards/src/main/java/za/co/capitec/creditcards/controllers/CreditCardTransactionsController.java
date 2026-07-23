package za.co.capitec.creditcards.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.creditcards.dtos.requests.CreditCardTransactionDto;
import za.co.capitec.creditcards.dtos.response.CreditCardAccountTransaction;
import za.co.capitec.creditcards.dtos.response.CreditCardTransactionResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@Slf4j
public class CreditCardTransactionsController {

//    private final ICreditCardTransaction service;
//
//    public CreditCardTransactionsController(final ICreditCardTransaction service) {
//        this.service = service;
//    }

    //-- save a single credit card CREDIT/DEBIT transaction api
    //-- http://localhost:8080/api/v1/transaction
    @PostMapping
    public ResponseEntity<String> handleTransaction(@RequestBody CreditCardTransactionDto transactionDto) {
        log.info("Transaction request received: {}", transactionDto);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    //-- get all transactions for a card between startDate and endDate
    //-- http://localhost:8080/api/v1/transaction/{cardNumber}/{startDate}/{endDate}?pageNo=0&pageSize=10&sortBy=date&sortDir=desc
    @GetMapping("/{cardNumber}/{startDate}/{endDate}")
    public ResponseEntity<CreditCardTransactionResponse> handleFindAll(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "Date", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir,
            @PathVariable("cardNumber") Long cardNumber,
            @PathVariable("startDate") LocalDate startDate,
            @PathVariable("endDate") LocalDate endDate) {
        log.info("Find all transactions for cardNumber: {} from {} to {}", cardNumber, startDate, endDate);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    //-- get all transactions by idNumber between startDate and endDate
    //-- http://localhost:8080/api/v1/transaction/{idNumber}?startDate={startDate}&endDate={endDate}&pageNo=0&pageSize=10
    @GetMapping("/{idNumber}")
    public ResponseEntity<List<CreditCardAccountTransaction>> handleFindAllByIdNumber(
            @PathVariable("idNumber") Long idNumber,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "date", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        //TODO: For each credit card from the idNumber, fetch transactions, build response, then convert to list
        log.info("Find all credit card transactions for idNumber: {}, from:{} to: {}", idNumber, startDate, endDate);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}

