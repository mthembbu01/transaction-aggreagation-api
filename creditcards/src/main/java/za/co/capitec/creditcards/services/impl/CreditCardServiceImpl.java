package za.co.capitec.creditcards.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import za.co.capitec.creditcards.constants.CreditCardsConstants;
import za.co.capitec.creditcards.dtos.records.CreditCardRecord;
import za.co.capitec.creditcards.dtos.requests.CreateCreditCardDto;
import za.co.capitec.creditcards.dtos.requests.UpdateCreditCardDto;
import za.co.capitec.creditcards.dtos.response.ResponseDto;
import za.co.capitec.creditcards.entity.CreditCards;
import za.co.capitec.creditcards.exceptions.ResourceAlreadyExistsException;
import za.co.capitec.creditcards.exceptions.ResourceNotFoundException;
import za.co.capitec.creditcards.repositories.CreditCardRepository;
import za.co.capitec.creditcards.services.ICreditCardService;
import za.co.capitec.creditcards.utilities.CreditCardUtils;
import za.co.capitec.creditcards.utilities.dates.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements ICreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final ModelMapper modelMapper;
    /**
     *
     * @param createCreditCardDto
     * @return
     */
    @Override
    public ResponseDto createCreditCard(CreateCreditCardDto createCreditCardDto) {
        //-- 1. Map the CreateCreditCardDto to CreditCards
        CreditCards creditCard = modelMapper.map(createCreditCardDto, CreditCards.class);
        //-- If one of the customer unique attributes exists, it becomes an unprocessable entity
        isExist(createCreditCardDto.getIdNumber(), createCreditCardDto.getMobileNumber());
        //-- 2. Save the newly created credit card
        creditCard.setCardNumber(CreditCardUtils.generateCardNumber());
        creditCard.setAccountNumber(CreditCardUtils.generateAccNumber());
        creditCard.setAmount(createCreditCardDto.getCreditLimit()); // credit card balance
        creditCard.setAvailableCredit(createCreditCardDto.getCreditLimit());
        creditCard.setMinimumPayment(BigDecimal.ZERO);
        creditCard.setOutstandingBalance(BigDecimal.ZERO);
        creditCard.setActiveSw(true);
        creditCardRepository.save(creditCard);
        //-- 3. Return the response
        return new ResponseDto(CreditCardsConstants.STATUS_201, CreditCardsConstants.MESSAGE_201);
    }
    /**
     *
     * @param cardNumber
     * @return
     */
    @Override
    public CreditCardRecord findByCardNumber(Long cardNumber) {
        //-- 1. Find the credit card by cardNumber
        CreditCards creditCard = findCreditCard(cardNumber);
        //-- 2. Return the CreditCard Record
        return modelMapper.map(creditCard,CreditCardRecord.class);
    }
    /**
     *
     * @param idNumber
     * @return
     */
    @Override
    public List<CreditCardRecord> findCreditCardsByIdNumber(String idNumber) {
        //-- does the customer exist by idNumber
        if (!creditCardRepository.existsByIdNumber(idNumber))
            throw new ResourceNotFoundException("CreditCard", "ID Number", idNumber);
        //-- 1. Find the credit cards by ID number
        return creditCardRepository.findAllByIdNumber(idNumber).stream()
                .filter(CreditCards::isActiveSw)
                .map(creditCard -> modelMapper.map(creditCard,CreditCardRecord.class))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param cardNumber
     * @param updateCreditCardDto
     * @return
     */
    @Override
    public ResponseDto updateCreditCardByCardNumber(Long cardNumber, UpdateCreditCardDto updateCreditCardDto) {
        //-- 1. Find the credit card by cardNumber
        CreditCards creditCard = findCreditCard(cardNumber);
        //-- 2. Extract update values
        String mobileNumber = updateCreditCardDto.getMobileNumber();
        String idNumber = updateCreditCardDto.getIdNumber();
        BigDecimal creditLimit = updateCreditCardDto.getCreditLimit();
        LocalDate expiryDate = updateCreditCardDto.getExpiryDate();
        boolean activeSw = updateCreditCardDto.isActiveSw();
        //-- 3. Validate unique fields
        isExist(idNumber, mobileNumber);
        //-- 4. Update attributes
        creditCard.setMobileNumber(mobileNumber);
        creditCard.setIdNumber(idNumber);
        creditCard.setCreditLimit(creditLimit);
        creditCard.setExpiryDate(expiryDate);
        creditCard.setActiveSw(activeSw);
        //-- update the CreditCard object
        creditCardRepository.save(creditCard);
        //-- 5. return a proper message
        return new ResponseDto(CreditCardsConstants.STATUS_200, CreditCardsConstants.MESSAGE_200);
    }
    /**
     *
     * @param cardNumber
     * @return
     */
    @Override
    public ResponseDto deleteCreditCardByCardNumber(Long cardNumber) {
        //-- 1. Find the credit card by cardNumber
        CreditCards creditCard = findCreditCard(cardNumber);
        //-- 2. Soft delete
        creditCard.setAvailableCredit(BigDecimal.ZERO);
        creditCard.setExpiryDate(DateUtils.getCurrentDate());
        creditCard.setActiveSw(false);
        creditCardRepository.save(creditCard);
        //-- 3. return a proper message
        return new ResponseDto(CreditCardsConstants.STATUS_204, CreditCardsConstants.MESSAGE_204);
    }
    /**
     * Check whether the Credit Card with unique fields - ID Number, Mobile Number - already exists
     * @param idNumber
     * @param mobileNumber
     * @return
     */
    private void isExist(String idNumber, String mobileNumber) {
        boolean isExistByIdNumber = creditCardRepository.existsByIdNumber(idNumber);
        boolean isExistByMobileNumber = creditCardRepository.existsByMobileNumber(mobileNumber);
        //-- ID number exists
        if (isExistByIdNumber) {
            throw new ResourceAlreadyExistsException("CreditCard", "ID Number", idNumber);
        }
        //-- Mobile number exists
        if (isExistByMobileNumber) {
            throw new ResourceAlreadyExistsException("CreditCard", "Mobile Number", mobileNumber);
        }
    }
    /**
     *
     * @param cardNumber
     * @return
     */
    @Override
    public CreditCards findCreditCard(Long cardNumber) {
        return creditCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("CreditCard", "Card Number", String.valueOf(cardNumber)));
    }
    /**
     *
     * @param creditCard
     * @return
     */
    @Override
    public ResponseDto saveCreditCard(CreditCards creditCard) {
        creditCardRepository.save(creditCard);
        return new ResponseDto(CreditCardsConstants.STATUS_200, CreditCardsConstants.MESSAGE_200);
    }
}

