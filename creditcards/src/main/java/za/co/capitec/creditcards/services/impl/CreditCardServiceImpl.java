package za.co.capitec.creditcards.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import za.co.capitec.creditcards.constants.CreditCardsConstants;
import za.co.capitec.creditcards.dtos.records.CreditCardRecord;
import za.co.capitec.creditcards.dtos.requests.CreateCreditCardDto;
import za.co.capitec.creditcards.dtos.requests.UpdateCreditCardDto;
import za.co.capitec.creditcards.dtos.response.ResponseDto;
import za.co.capitec.creditcards.entity.CreditCards;
import za.co.capitec.creditcards.enums.CreditCardType;
import za.co.capitec.creditcards.exceptions.ResourceAlreadyExistsException;
import za.co.capitec.creditcards.exceptions.ResourceNotFoundException;
import za.co.capitec.creditcards.repositories.CreditCardRepository;
import za.co.capitec.creditcards.services.ICreditCardService;
import za.co.capitec.creditcards.utilities.CreditCardUtils;
import za.co.capitec.creditcards.utilities.dates.DateUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditCardServiceImpl implements ICreditCardService {

    private final String ACCOUNTS_CACHE_KEY = "accounts";
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
        isExist(createCreditCardDto.idNumber(), createCreditCardDto.mobileNumber());
        //-- 2. Save the newly created credit card
        creditCard.setCardNumber(CreditCardUtils.generateCardNumber());
        creditCard.setAccountNumber(CreditCardUtils.generateAccNumber());
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
        String mobileNumber = updateCreditCardDto.mobileNumber();
        String idNumber = updateCreditCardDto.idNumber();
        Double creditLimit = updateCreditCardDto.creditLimit();
        LocalDate expiryDate = updateCreditCardDto.expiryDate();
        boolean activeSw = updateCreditCardDto.activeSw();
        //-- 3. Validate unique fields
        isExist(idNumber, mobileNumber);
        //-- 4. Update attributes
        creditCard.setMobileNumber(mobileNumber);
        creditCard.setIdNumber(idNumber);
        creditCard.setCreditLimit(creditLimit);
        creditCard.setExpiryDate(expiryDate);
        creditCard.setActiveSw(activeSw);
        //-- update the CreditCard object
        CreditCards updatedCreditCard = creditCardRepository.save(creditCard);
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
        creditCard.setAvailableCredit(0.0);
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
    private CreditCards findCreditCard(Long cardNumber) {
        return creditCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("CreditCard", "Card Number", String.valueOf(cardNumber)));
    }
}

