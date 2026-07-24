package za.co.capitec.creditcards.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import za.co.capitec.creditcards.constants.CreditCardsConstants;
import za.co.capitec.creditcards.dtos.records.CreditCardRecord;
import za.co.capitec.creditcards.dtos.requests.CreateCreditCardDto;
import za.co.capitec.creditcards.dtos.requests.UpdateCreditCardDto;
import za.co.capitec.creditcards.dtos.response.CreditCardsResponse;
import za.co.capitec.creditcards.dtos.response.ResponseDto;
import za.co.capitec.creditcards.entity.CreditCards;
import za.co.capitec.creditcards.enums.CreditCardType;
import za.co.capitec.creditcards.exceptions.ResourceAlreadyExistsException;
import za.co.capitec.creditcards.exceptions.ResourceNotFoundException;
import za.co.capitec.creditcards.repositories.CreditCardRepository;
import za.co.capitec.creditcards.services.ICreditCardService;
import za.co.capitec.creditcards.utilities.CreditCardUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements ICreditCardService {

    private final CreditCardRepository creditCardRepository;

    @Override
    public CreditCardsResponse findAll(Pageable pageable) {
        Page<CreditCards> page = creditCardRepository.findAll(pageable);
        return CreditCardsResponse.builder()
                .content(page.getContent().stream().map(this::toRecord).toList())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .isLast(page.isLast())
                .build();
    }

    @Override
    public CreditCardRecord findByCardNumber(Long cardNumber) {
        CreditCards creditCard = creditCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("CreditCard", "cardNumber", String.valueOf(cardNumber)));
        return toRecord(creditCard);
    }

    @Override
    public List<CreditCardRecord> findCreditCardsByIdNumber(String idNumber) {
        if (!creditCardRepository.existsByIdNumber(idNumber)) {
            throw new ResourceNotFoundException("CreditCard", "idNumber", idNumber);
        }

        return creditCardRepository.findAllByIdNumber(idNumber).stream()
                .filter(CreditCards::isActiveSw)
                .map(this::toRecord)
                .toList();
    }

    @Override
    public ResponseDto createCreditCard(CreateCreditCardDto createCreditCardDto) {
        validateUniqueFields(createCreditCardDto.idNumber(), createCreditCardDto.mobileNumber());

        CreditCards creditCard = CreditCards.builder()
                .cardNumber(Long.parseLong(CreditCardUtils.generateCardNumber()))
                .cardType(parseCardType(createCreditCardDto.cardType()))
                .mobileNumber(createCreditCardDto.mobileNumber())
                .idNumber(createCreditCardDto.idNumber())
                .creditLimit(createCreditCardDto.creditLimit())
                .availableCredit(createCreditCardDto.creditLimit())
                .activeSw(true)
                .build();

        creditCardRepository.save(creditCard);

        return ResponseDto.builder()
                .statusCode(CreditCardsConstants.STATUS_201)
                .statusMsg(CreditCardsConstants.MESSAGE_201)
                .build();
    }

    @Override
    public ResponseDto updateCreditCardByCardNumber(Long cardNumber, UpdateCreditCardDto updateCreditCardDto) {
        CreditCards creditCard = creditCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("CreditCard", "cardNumber", String.valueOf(cardNumber)));

        creditCard.setCardType(updateCreditCardDto.cardType());
        creditCard.setMobileNumber(updateCreditCardDto.mobileNumber());
        creditCard.setIdNumber(updateCreditCardDto.idNumber());
        creditCard.setCreditLimit(updateCreditCardDto.creditLimit());
        creditCard.setActiveSw(updateCreditCardDto.activeSw());

        creditCardRepository.save(creditCard);

        return ResponseDto.builder()
                .statusCode(CreditCardsConstants.STATUS_200)
                .statusMsg(CreditCardsConstants.MESSAGE_200)
                .build();
    }

    @Override
    public ResponseDto deleteCreditCardByCardNumber(Long cardNumber) {
        CreditCards creditCard = creditCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("CreditCard", "cardNumber", String.valueOf(cardNumber)));

        creditCard.setActiveSw(false);
        creditCardRepository.save(creditCard);

        return ResponseDto.builder()
                .statusCode(CreditCardsConstants.STATUS_204)
                .statusMsg(CreditCardsConstants.MESSAGE_204)
                .build();
    }

    private void validateUniqueFields(String idNumber, String mobileNumber) {
        if (creditCardRepository.existsByIdNumber(idNumber)) {
            throw new ResourceAlreadyExistsException("CreditCard", "idNumber", idNumber);
        }
        if (creditCardRepository.existsByMobileNumber(mobileNumber)) {
            throw new ResourceAlreadyExistsException("CreditCard", "mobileNumber", mobileNumber);
        }
    }

    private CreditCardType parseCardType(String cardType) {
        return CreditCardType.valueOf(cardType.trim().toUpperCase());
    }

    private CreditCardRecord toRecord(CreditCards creditCard) {
        return new CreditCardRecord(
                creditCard.getCardNumber(),
                creditCard.getCardType(),
                creditCard.getMobileNumber(),
                creditCard.getIdNumber(),
                creditCard.getCreditLimit(),
                creditCard.getAvailableCredit(),
                creditCard.isActiveSw()
        );
    }
}

