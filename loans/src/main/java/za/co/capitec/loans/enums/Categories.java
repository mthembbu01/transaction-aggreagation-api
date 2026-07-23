package za.co.capitec.loans.enums;

import lombok.Getter;

@Getter
public enum Categories {
    FOOD("Food", "food"),
    TRANSPORT("Transport", "transport"),
    SHOPPING("Shopping", "shopping"),
    UTILITIES("Utilities", "utilities"),
    INSURANCE("Insurance", "insurance"),
    MEDICAL("Medical", "medical"),
    SALARY("Salary", "salary"),
    TRANSFER("Transfer", "transfer"),
    UNKNOWN("Unknown", "unknown"),
    CREDIT("Credit", "credit"),
    DEBIT("Debit", "debit"),
    DEPOSIT("Deposit", "deposit"),
    WITHDRAWAL("Withdrawal", "withdrawal"),
    INTEREST("Interest", "interest"),
    FEE("Fee", "fee"),
    REPAYMENT("Repayment", "repayment"),
    DISBURSEMENT("Disbursement", "disbursement");

    private final String value;
    private final String description;

    Categories(final String value, final String description) {
        this.value = value;
        this.description = description;
    }
}

