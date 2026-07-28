package za.co.capitec.creditcards.services;

import za.co.capitec.creditcards.dtos.records.CreditCardRecord;
import za.co.capitec.creditcards.dtos.requests.CreateCreditCardDto;
import za.co.capitec.creditcards.dtos.requests.UpdateCreditCardDto;
import za.co.capitec.creditcards.dtos.response.ResponseDto;
import za.co.capitec.creditcards.entity.CreditCards;

import java.util.List;

public interface ICreditCardService {
	CreditCardRecord findByCardNumber(Long cardNumber);
	ResponseDto saveCreditCard(CreditCards creditCard);
	CreditCards findCreditCard(Long cardNumber);
	List<CreditCardRecord> findCreditCardsByIdNumber(String idNumber);
	ResponseDto createCreditCard(CreateCreditCardDto createCreditCardDto);
	ResponseDto updateCreditCardByCardNumber(Long cardNumber, UpdateCreditCardDto updateCreditCardDto);
	ResponseDto deleteCreditCardByCardNumber(Long cardNumber);
}

