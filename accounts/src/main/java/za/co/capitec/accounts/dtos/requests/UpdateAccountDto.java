package za.co.capitec.accounts.dtos.requests;

import za.co.capitec.accounts.enums.AccountType;

public record UpdateAccountDto(Long accountNumber,
                               AccountType accountType, // SAVINGS, CHEQUE, Transactional
                               String mobileNumber,
                               String idNumber,
                               String branchAddress,
                               boolean activeSw) {
}
