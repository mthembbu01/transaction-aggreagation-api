package za.co.capitec.creditcards.dtos.records;

import za.co.capitec.creditcards.enums.CreditCardType;

public record CreditCardRecord(Long cardNumber,
                                CreditCardType cardType,
                                String mobileNumber,
                                String idNumber,
                                Double creditLimit,
                                Double availableCredit,
                                boolean activeSw) {
}

