package za.co.capitec.api_gateway.enums;


import lombok.Getter;

@Getter
public enum AccountType {

    SAVINGS("Savings","savings"),
    CHEQUE("Cheque","cheque"),
    Transaction("Transaction","transaction"),
    Business("Business","business"),
    ;

    private final String value;
    private final String description;
    AccountType(final String value, final String description) {
        this.value = value;
        this.description = description;
    }
}
