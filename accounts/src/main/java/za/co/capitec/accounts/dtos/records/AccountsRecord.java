package za.co.capitec.accounts.dtos.records;


import za.co.capitec.accounts.enums.AccountType;

public record AccountsRecord(Long accountNumber,
                             AccountType accountType, // SAVINGS, CHEQUE, Transactional, Business
                             String mobileNumber,
                             String idNumber,
                             String branchAddress,
                             boolean activeSw) {
}
