package za.co.capitec.loans.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.loans.dtos.requests.LoanTransactionDto;
import za.co.capitec.loans.dtos.response.LoanAccountTransaction;
import za.co.capitec.loans.dtos.response.LoanTransactionResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@Slf4j
public class LoanTransactionsController {

    //-- save a single loan transaction api
    //-- http://localhost:8080/api/v1/transaction
    @PostMapping
    public ResponseEntity<String> handleTransaction(@RequestBody LoanTransactionDto transactionDto) {
        log.info("Loan transaction request received: {}", transactionDto);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    //-- get all transactions for a loan between startDate and endDate
    //-- http://localhost:8080/api/v1/transaction/{loanNumber}/{startDate}/{endDate}?pageNo=0&pageSize=10&sortBy=date&sortDir=desc
    @GetMapping("/{loanNumber}/{startDate}/{endDate}")
    public ResponseEntity<LoanTransactionResponse> handleFindAll(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "Date", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir,
            @PathVariable("loanNumber") Long loanNumber,
            @PathVariable("startDate") LocalDate startDate,
            @PathVariable("endDate") LocalDate endDate) {
        log.info("Find all transactions for loanNumber: {} from {} to {}", loanNumber, startDate, endDate);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    //-- get all transactions by idNumber between startDate and endDate
    //-- http://localhost:8080/api/v1/transaction/{idNumber}?startDate={startDate}&endDate={endDate}&pageNo=0&pageSize=10
    @GetMapping("/{idNumber}")
    public ResponseEntity<List<LoanAccountTransaction>> handleFindAllByIdNumber(
            @PathVariable("idNumber") Long idNumber,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "date", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        log.info("Find all loan transactions for idNumber: {}, from:{} to: {}", idNumber, startDate, endDate);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}

