package za.co.capitec.creditcards.dtos.response;

import lombok.Builder;
import za.co.capitec.creditcards.enums.CreditCardType;

import java.math.BigDecimal;

@Builder
public record CreditCardAccountTransaction(
        Long cardNumber,
        CreditCardType cardType,
        BigDecimal currentBalance,
        CreditCardTransactionResponse transactions
) {
}

