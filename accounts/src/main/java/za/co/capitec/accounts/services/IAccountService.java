package za.co.capitec.accounts.services;


import za.co.capitec.accounts.entity.Accounts;
import za.co.capitec.coreapi.dtos.accounts.records.AccountsRecord;
import za.co.capitec.coreapi.dtos.accounts.requests.CreateAccountsDto;
import za.co.capitec.coreapi.dtos.accounts.requests.UpdateAccountDto;
import za.co.capitec.coreapi.dtos.ResponseDto;


import java.util.List;

public interface IAccountService {
    ResponseDto createAccount(CreateAccountsDto createAccountsDto);
    ResponseDto updateAccountByAccNumber(Long accountNumber, UpdateAccountDto updateAccountDto);
    ResponseDto deleteAccountByAccNumber(Long accountNumber);
    AccountsRecord findByAccNumber(Long accountNumber);
    Accounts findAccounts(Long accountNumber);
    ResponseDto saveAccount(Accounts account);
    List<AccountsRecord> findAccountsByIdNumber(String idNumber);
}
