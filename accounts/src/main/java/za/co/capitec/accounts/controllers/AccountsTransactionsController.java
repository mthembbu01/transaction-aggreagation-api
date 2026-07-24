package za.co.capitec.accounts.controllers;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.accounts.dtos.requests.TransactionDto;
import za.co.capitec.accounts.dtos.requests.TransferDto;
import za.co.capitec.accounts.dtos.response.AccountTransaction;
import za.co.capitec.accounts.dtos.response.ResponseDto;
import za.co.capitec.accounts.dtos.response.TransactionResponse;
import za.co.capitec.accounts.services.ITransactionService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
@Slf4j
public class AccountsTransactionsController {

    private final ITransactionService service;

    //-- save a single account transaction api
    //-- http://localhost:8090/api/v1/
    /**
     *  Handle a transaction request
     * @param transactionDto
     * @return
     */
    @PostMapping
    public ResponseEntity<ResponseDto> handleTransact(@RequestBody TransactionDto transactionDto) {
        log.info("Transaction request received: {}", transactionDto);
        return new ResponseEntity<>(service.transact(transactionDto), HttpStatus.OK);
    }
    //-- Typical Bank Statement
    //-- get all transactions using the account number between startDate and endDate api
    //-- http://localhost:8091/api/v1/{accountNumber}/{startDate}/{endDate}?pageNo=0&pageSize=10&sortBy=date&sortDir=asc
    /**
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @param idNumber
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/{idNumber}/{startDate}/{endDate}")
    public ResponseEntity<TransactionResponse> handleFindAll(@PathVariable("idNumber") @Pattern(regexp = "\\d{13}") String idNumber,
                                                             @PathVariable("startDate") LocalDate startDate,
                                                             @PathVariable("endDate") LocalDate endDate,
                                                             @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                             @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                             @RequestParam(value = "sortBy", defaultValue = "date", required = false) String sortBy,
                                                             @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        log.info("Find all transactions for idNumber: {} from: {} to: {}", idNumber, startDate, endDate);
        return new ResponseEntity<>(service.findAllByIdNumber(pageNo, pageSize, sortBy, sortDir, idNumber, startDate, endDate), HttpStatus.OK);
    }
}
