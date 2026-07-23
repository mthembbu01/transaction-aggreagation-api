package za.co.capitec.loans.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.loans.dtos.requests.CreateLoanDto;
import za.co.capitec.loans.dtos.requests.UpdateLoanDto;
import za.co.capitec.loans.dtos.response.LoansResponse;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1")
public class LoansController {

    //-- http://localhost:8080/api/v1?pageNo=0&pageSize=10&sortBy=loanType&sortDir=asc
    @GetMapping
    public ResponseEntity<LoansResponse> handleFindAll(@RequestParam(defaultValue = "0") int pageNo,
                                                        @RequestParam(defaultValue = "10") int pageSize,
                                                        @RequestParam(defaultValue = "loanType") String sortBy,
                                                        @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Received find all loans request");
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/{loanNumber}
    @GetMapping(path = "/{loanNumber}")
    public ResponseEntity<Object> handleFindById(@PathVariable("loanNumber") Long loanNumber) {
        log.info("Received find loan by loanNumber: {}", loanNumber);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/search?idNumber=123456
    @GetMapping(path = "/search")
    public ResponseEntity<Object> handleFindByIDNumber(@RequestParam("idNumber") String idNumber) {
        log.info("Received find loan by ID Number: {}", idNumber);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/
    @PostMapping
    public ResponseEntity<Object> handleCreate(@RequestBody CreateLoanDto createLoanDto) {
        log.info("Received create new loan request {}", createLoanDto);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    //-- http://localhost:8080/api/v1/{loanNumber}
    @PutMapping(path = "/{loanNumber}")
    public ResponseEntity<Object> handleUpdate(@PathVariable("loanNumber") Long loanNumber,
                                               @RequestBody UpdateLoanDto updateLoanDto) {
        log.info("Received the update request of loanNumber: {}", loanNumber);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/{loanNumber}
    @PatchMapping(path = "/{loanNumber}")
    public ResponseEntity<Object> handlePatch(@PathVariable("loanNumber") Long loanNumber,
                                              @RequestBody UpdateLoanDto updateLoanDto) {
        log.info("Received the patch request of loanNumber: {}", loanNumber);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{loanNumber}")
    public void handleDelete(@PathVariable("loanNumber") Long loanNumber) {
        log.info("Received the delete request of loanNumber: {}", loanNumber);
    }
}

