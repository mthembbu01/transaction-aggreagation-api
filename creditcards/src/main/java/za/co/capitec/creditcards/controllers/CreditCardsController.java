package za.co.capitec.creditcards.controllers;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.creditcards.dtos.records.CreditCardRecord;
import za.co.capitec.creditcards.dtos.requests.CreateCreditCardDto;
import za.co.capitec.creditcards.dtos.requests.UpdateCreditCardDto;
import za.co.capitec.creditcards.dtos.response.ResponseDto;
import za.co.capitec.creditcards.services.ICreditCardService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class CreditCardsController {

    private final ICreditCardService service;

    //-- http://localhost:8080/api/v1/{cardNumber}
    @GetMapping(path = "/{cardNumber}")
    public ResponseEntity<CreditCardRecord> handleFindById(@PathVariable("cardNumber") Long cardNumber) {
        log.info("Received find credit card by cardNumber: {}", cardNumber);
        return new ResponseEntity<>(service.findByCardNumber(cardNumber), HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/creditcards?idNumber=1234567890123
    @GetMapping(path = "/creditcards")
    public ResponseEntity<List<CreditCardRecord>> handleFindByIDNumber(@RequestParam("idNumber") @Pattern(regexp = "\\d{13}") String idNumber) {
        log.info("Received find credit card by ID Number: {}", idNumber);
        return new ResponseEntity<>(service.findCreditCardsByIdNumber(idNumber), HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/
    @PostMapping
    public ResponseEntity<ResponseDto> handleCreate(@RequestBody CreateCreditCardDto createCreditCardDto) {
        log.info("Received create new credit card request {}", createCreditCardDto);
        return new ResponseEntity<>(service.createCreditCard(createCreditCardDto), HttpStatus.CREATED);
    }

    //-- http://localhost:8080/api/v1/{cardNumber}
    @PutMapping(path = "/{cardNumber}")
    public ResponseEntity<ResponseDto> handleUpdate(@PathVariable("cardNumber") Long cardNumber,
                                                    @RequestBody UpdateCreditCardDto updateCreditCardDto) {
        log.info("Received the update request of cardNumber: {}", cardNumber);
        return new ResponseEntity<>(service.updateCreditCardByCardNumber(cardNumber, updateCreditCardDto), HttpStatus.OK);
    }

    //-- http://localhost:8080/api/v1/{cardNumber}
    @PatchMapping(path = "/{cardNumber}")
    public ResponseEntity<ResponseDto> handlePatch(@PathVariable("cardNumber") Long cardNumber,
                                                   @RequestBody UpdateCreditCardDto updateCreditCardDto) {
        log.info("Received the patch request of cardNumber: {}", cardNumber);
        return new ResponseEntity<>(service.updateCreditCardByCardNumber(cardNumber, updateCreditCardDto), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{cardNumber}")
    public ResponseEntity<ResponseDto> handleDelete(@PathVariable("cardNumber") Long cardNumber) {
        log.info("Received the delete request of cardNumber: {}", cardNumber);
        return new ResponseEntity<>(service.deleteCreditCardByCardNumber(cardNumber), HttpStatus.OK);
    }
}

