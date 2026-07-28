package za.co.capitec.creditcards.controllers;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.creditcards.dtos.requests.CreditCardTransactionDto;
import za.co.capitec.creditcards.dtos.response.CreditCardTransactionResponse;
import za.co.capitec.creditcards.dtos.response.ResponseDto;
import za.co.capitec.creditcards.services.ICCTransactionService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
@Slf4j
public class CreditCardTransactionsController {

    private final ICCTransactionService service;
    /**
     *
     * @param transactionDto
     * @return
     */
    //-- save a single credit card CREDIT/DEBIT transaction api
    //-- http://localhost:8080/api/v1/transaction
    @PostMapping
    public ResponseEntity<ResponseDto> handleTransact(@RequestBody CreditCardTransactionDto transactionDto) {
        log.info("Transaction request received: {}", transactionDto);
        return new ResponseEntity<>(service.transact(transactionDto), HttpStatus.OK);
    }
    /**
     *
     * @param idNumber
     * @param startDate
     * @param endDate
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    //-- get all transactions by idNumber between startDate and endDate
    //-- http://localhost:8080/api/v1/transaction/{idNumber}?startDate={startDate}&endDate={endDate}&pageNo=0&pageSize=10
    @GetMapping("/{idNumber}/{startDate}/{endDate}")
    public ResponseEntity<CreditCardTransactionResponse> handleFindAllByIdNumber(@PathVariable("idNumber") @Pattern(regexp = "\\d{13}") String idNumber,
                                                                               @PathVariable("startDate") LocalDate startDate,
                                                                               @PathVariable("endDate") LocalDate endDate,
                                                                               @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                               @RequestParam(value = "sortBy", defaultValue = "date", required = false) String sortBy,
                                                                               @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        log.info("Find all credit card transactions for idNumber: {}, from:{} to: {}", idNumber, startDate, endDate);
        return new ResponseEntity<>(service.findAllByIdNumber(pageNo, pageSize, sortBy, sortDir, idNumber, startDate, endDate), HttpStatus.OK);
    }
}

