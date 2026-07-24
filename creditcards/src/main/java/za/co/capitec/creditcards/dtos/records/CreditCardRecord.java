package za.co.capitec.creditcards.dtos.records;

import za.co.capitec.creditcards.enums.CreditCardType;

import java.time.LocalDate;

public record CreditCardRecord(Long cardNumber,
                                CreditCardType cardType,
                                String mobileNumber,
                                String idNumber,
                                Double creditLimit,
                                Double availableCredit,
                               Double outstandingBalance,
                               Double minimumPayment,
                               LocalDate issueDate,
                               LocalDate expiryDate,
                                boolean activeSw) {
}

