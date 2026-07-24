package za.co.capitec.creditcards.services;

import org.springframework.data.domain.Pageable;
import za.co.capitec.creditcards.dtos.records.CreditCardRecord;
import za.co.capitec.creditcards.dtos.requests.CreateCreditCardDto;
import za.co.capitec.creditcards.dtos.requests.UpdateCreditCardDto;
import za.co.capitec.creditcards.dtos.response.CreditCardsResponse;
import za.co.capitec.creditcards.dtos.response.ResponseDto;

import java.util.List;

public interface ICreditCardService {
	CreditCardsResponse findAll(Pageable pageable);
	CreditCardRecord findByCardNumber(Long cardNumber);
	List<CreditCardRecord> findCreditCardsByIdNumber(String idNumber);
	ResponseDto createCreditCard(CreateCreditCardDto createCreditCardDto);
	ResponseDto updateCreditCardByCardNumber(Long cardNumber, UpdateCreditCardDto updateCreditCardDto);
	ResponseDto deleteCreditCardByCardNumber(Long cardNumber);
}

