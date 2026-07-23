package za.co.capitec.loans.enums;

import lombok.Getter;

@Getter
public enum LoanType {

    PERSONAL("Personal", "personal"),
    HOME("Home", "home"),
    VEHICLE("Vehicle", "vehicle"),
    BUSINESS("Business", "business"),
    STUDENT("Student", "student"),
    ;

    private final String value;
    private final String description;

    LoanType(final String value, final String description) {
        this.value = value;
        this.description = description;
    }
}

