package za.co.capitec.coreapi.enums.creditcards;

import lombok.Getter;

@Getter
public enum CreditCardType {

    VISA("Visa", "visa"),
    MASTERCARD("Mastercard", "mastercard"),
    AMEX("Amex", "amex"),
    ;

    private final String value;
    private final String description;

    CreditCardType(final String value, final String description) {
        this.value = value;
        this.description = description;
    }
}

