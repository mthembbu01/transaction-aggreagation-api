package za.co.capitec.creditcards.dtos.requests;

import za.co.capitec.creditcards.enums.CreditCardType;

public record UpdateCreditCardDto(Long cardNumber,
                                  CreditCardType cardType,
                                  String mobileNumber,
                                  String idNumber,
                                  Double creditLimit,
                                  boolean activeSw) {
}

