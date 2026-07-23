package za.co.capitec.loans.enums;

import lombok.Getter;

@Getter
public enum LoanStatus {

    ACTIVE("Active", "active"),
    CLOSED("Closed", "closed"),
    DEFAULTED("Defaulted", "defaulted"),
    PENDING("Pending", "pending"),
    ;

    private final String value;
    private final String description;

    LoanStatus(final String value, final String description) {
        this.value = value;
        this.description = description;
    }
}

