package za.co.capitec.loans.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.loans.dtos.records.LoanRecord;
import za.co.capitec.loans.dtos.requests.CreateLoanDto;
import za.co.capitec.loans.dtos.requests.UpdateLoanDto;
import za.co.capitec.loans.dtos.response.LoansResponse;
import za.co.capitec.loans.dtos.response.ResponseDto;
import za.co.capitec.loans.services.ILoanService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class LoansController {

    private final ILoanService service;

    //-- http://localhost:8080/api/v1?pageNo=0&pageSize=10&sortBy=loanType&sortDir=asc
    @GetMapping
    public ResponseEntity<LoansResponse> handleFindAll(@RequestParam(defaultValue = "0") int pageNo,
                                                        @RequestParam(defaultValue = "10") int pageSize,
                                                        @RequestParam(defaultValue = "loanType") String sortBy,
                                                        @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Received find all loans request");
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return new ResponseEntity<>(service.findAll(pageable), HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/{loanNumber}
    @GetMapping(path = "/{loanNumber}")
    public ResponseEntity<LoanRecord> handleFindById(@PathVariable("loanNumber") Long loanNumber) {
        log.info("Received find loan by loanNumber: {}", loanNumber);
        return new ResponseEntity<>(service.findByLoanNumber(loanNumber), HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/search?idNumber=123456
    @GetMapping(path = "/search")
    public ResponseEntity<List<LoanRecord>> handleFindByIDNumber(@RequestParam("idNumber") String idNumber) {
        log.info("Received find loan by ID Number: {}", idNumber);
        return new ResponseEntity<>(service.findLoansByIdNumber(idNumber), HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/
    @PostMapping
    public ResponseEntity<ResponseDto> handleCreate(@RequestBody CreateLoanDto createLoanDto) {
        log.info("Received create new loan request {}", createLoanDto);
        return new ResponseEntity<>(service.createLoan(createLoanDto), HttpStatus.CREATED);
    }

    //-- http://localhost:8080/api/v1/{loanNumber}
    @PutMapping(path = "/{loanNumber}")
    public ResponseEntity<ResponseDto> handleUpdate(@PathVariable("loanNumber") Long loanNumber,
                                                    @RequestBody UpdateLoanDto updateLoanDto) {
        log.info("Received the update request of loanNumber: {}", loanNumber);
        return new ResponseEntity<>(service.updateLoanByLoanNumber(loanNumber, updateLoanDto), HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/{loanNumber}
    @PatchMapping(path = "/{loanNumber}")
    public ResponseEntity<ResponseDto> handlePatch(@PathVariable("loanNumber") Long loanNumber,
                                                   @RequestBody UpdateLoanDto updateLoanDto) {
        log.info("Received the patch request of loanNumber: {}", loanNumber);
        return new ResponseEntity<>(service.updateLoanByLoanNumber(loanNumber, updateLoanDto), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{loanNumber}")
    public ResponseEntity<ResponseDto> handleDelete(@PathVariable("loanNumber") Long loanNumber) {
        log.info("Received the delete request of loanNumber: {}", loanNumber);
        return new ResponseEntity<>(service.deleteLoanByLoanNumber(loanNumber), HttpStatus.OK);
    }
}

