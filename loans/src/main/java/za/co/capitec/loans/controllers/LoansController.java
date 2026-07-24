package za.co.capitec.loans.controllers;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.loans.dtos.records.LoanRecord;
import za.co.capitec.loans.dtos.requests.CreateLoanDto;
import za.co.capitec.loans.dtos.requests.UpdateLoanDto;
import za.co.capitec.loans.dtos.response.ResponseDto;
import za.co.capitec.loans.services.ILoanService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class LoansController {

    private final ILoanService service;

    /**
     * Find a loan by its loan number.
     * @param loanNumber the loan number
     * @return the loan record
     */
    //-- http://localhost:8080/api/v1/{loanNumber}
    @GetMapping(path = "/{loanNumber}")
    public ResponseEntity<LoanRecord> handleFindById(@PathVariable("loanNumber") Long loanNumber) {
        log.info("Received find loan by loanNumber: {}", loanNumber);
        return new ResponseEntity<>(service.findByLoanNumber(loanNumber), HttpStatus.OK);
    }
    /**
     *
     * @param idNumber
     * @return
     */
    //-- http://localhost:8080/api/v1/loans?idNumber=1234567890123
    @GetMapping(path = "/loans")
    public ResponseEntity<List<LoanRecord>> handleFindByIDNumber(@RequestParam("idNumber") @Pattern(regexp = "\\d{13}") String idNumber) {
        log.info("Received find loan by ID Number: {}", idNumber);
        return new ResponseEntity<>(service.findLoansByIdNumber(idNumber), HttpStatus.OK);
    }
    /**
     *
     * @param createLoanDto
     * @return
     */
    //-- http://localhost:8080/api/v1/
    @PostMapping
    public ResponseEntity<ResponseDto> handleCreate(@RequestBody CreateLoanDto createLoanDto) {
        log.info("Received create new loan request {}", createLoanDto);
        return new ResponseEntity<>(service.createLoan(createLoanDto), HttpStatus.CREATED);
    }
    /**
     *
     * @param loanNumber
     * @param updateLoanDto
     * @return
     */
    //-- http://localhost:8080/api/v1/{loanNumber}
    @PutMapping(path = "/{loanNumber}")
    public ResponseEntity<ResponseDto> handleUpdate(@PathVariable("loanNumber") Long loanNumber,
                                                    @RequestBody UpdateLoanDto updateLoanDto) {
        log.info("Received the update request of loanNumber: {}", loanNumber);
        return new ResponseEntity<>(service.updateLoanByLoanNumber(loanNumber, updateLoanDto), HttpStatus.OK);
    }
    /**
     * Partially update a loan by its loan number.
     * @param loanNumber the loan number
     * @param updateLoanDto the update loan DTO
     * @return the response DTO
     */
    //-- http://localhost:8080/api/v1/{loanNumber}
    @PatchMapping(path = "/{loanNumber}")
    public ResponseEntity<ResponseDto> handlePatch(@PathVariable("loanNumber") Long loanNumber,
                                                   @RequestBody UpdateLoanDto updateLoanDto) {
        log.info("Received the patch request of loanNumber: {}", loanNumber);
        return new ResponseEntity<>(service.updateLoanByLoanNumber(loanNumber, updateLoanDto), HttpStatus.OK);
    }
    /**
     * Delete a loan by its loan number.
     * @param loanNumber the loan number
     * @return the response DTO
     */
    @DeleteMapping(path = "/{loanNumber}")
    public ResponseEntity<ResponseDto> handleDelete(@PathVariable("loanNumber") Long loanNumber) {
        log.info("Received the delete request of loanNumber: {}", loanNumber);
        return new ResponseEntity<>(service.deleteLoanByLoanNumber(loanNumber), HttpStatus.NO_CONTENT);
    }
}

