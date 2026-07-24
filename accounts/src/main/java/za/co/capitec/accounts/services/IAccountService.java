package za.co.capitec.accounts.services;

import za.co.capitec.accounts.dtos.records.AccountsRecord;
import za.co.capitec.accounts.dtos.requests.CreateAccountsDto;
import za.co.capitec.accounts.dtos.requests.UpdateAccountDto;
import za.co.capitec.accounts.dtos.response.ResponseDto;
import za.co.capitec.accounts.entity.Accounts;
import za.co.capitec.accounts.enums.AccountType;

import java.util.List;

public interface IAccountService {
    ResponseDto createAccount(CreateAccountsDto  createAccountsDto);
    ResponseDto updateAccountByAccNumber(Long accountNumber, UpdateAccountDto updateAccountDto);
    ResponseDto deleteAccountByAccNumber(Long accountNumber);
    AccountsRecord findByAccNumber(Long accountNumber);
    List<AccountsRecord> findAccountsByIdNumber(String idNumber);
}
