package za.co.capitec.accounts.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.capitec.accounts.constants.AccountsConstants;
import za.co.capitec.accounts.entity.Accounts;
import za.co.capitec.accounts.repositories.AccountsRepository;
import za.co.capitec.accounts.services.IAccountService;
import za.co.capitec.accounts.utilities.AccountUtils;
import za.co.capitec.coreapi.dtos.ResponseDto;
import za.co.capitec.coreapi.dtos.accounts.records.AccountsRecord;
import za.co.capitec.coreapi.dtos.accounts.requests.CreateAccountsDto;
import za.co.capitec.coreapi.dtos.accounts.requests.UpdateAccountDto;
import za.co.capitec.coreapi.exceptions.ResourceAlreadyExistsException;
import za.co.capitec.coreapi.exceptions.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountsServiceImpl implements IAccountService {

    private final AccountsRepository accountsRepository;

    private final ModelMapper modelMapper;
    /**
     * The method to create an account for a customer. It first maps the CreateAccountsDto to Accounts entity,
     * checks if the account already exists based on unique attributes (ID Number and Mobile Number),
     * saves the new account, and returns a response indicating success.
     * @param createAccountsDto
     * @return
     */
    @Override
    public ResponseDto createAccount(CreateAccountsDto createAccountsDto) {
        //-- 1. Map the CreateAccountsDto to Accounts
        Accounts accounts = modelMapper.map(createAccountsDto, Accounts.class);
        //-- If one of the customer unique attributes exists, it becomes an unprocessable entity
        isExist(accounts.getIdNumber(), accounts.getMobileNumber());
        //-- 2. Save the newly created account
        accounts.setAccountNumber(AccountUtils.generateAccNumber());
        accounts.setActiveSw(true);
        accountsRepository.save(accounts);
        //-- 3. Return the response
        return new ResponseDto(AccountsConstants.MESSAGE_201, AccountsConstants.STATUS_201);
    }
    /**
     * Find Account number by accountNumber
     * @param accountNumber
     * @return
     */
    @Override
    public AccountsRecord findByAccNumber(Long accountNumber){
        //-- 1. Find the account by accountNumber
        Accounts accounts = findAccounts(accountNumber);
        //-- 2. Return the Accounts Record
        return modelMapper.map(accounts, AccountsRecord.class);
    }
    /**
     * Find Accounts by ID number
     * @param idNumber
     * @return
     */
    @Override
    public List<AccountsRecord> findAccountsByIdNumber(String idNumber){
        //-- 1. does the account exist by idNumber
        if(!accountsRepository.existsByIdNumber(idNumber))
            throw new ResourceNotFoundException("Customer Account","ID Number",idNumber);
        //-- 2. Return the Accounts Record
        return accountsRepository.findAllByIdNumber(idNumber)
                .stream()
                .filter(Accounts::isActiveSw)
                .map(account -> modelMapper.map(account, AccountsRecord.class))
                .collect(Collectors.toList());
    }
    /**
     * Update Account by accountNumber
     * @param accountNumber
     * @param updateAccountDto
     * @return
     */
    @Override
    public ResponseDto updateAccountByAccNumber(Long accountNumber, UpdateAccountDto updateAccountDto) {
        //-- 1. Find the account by accountNumber
        Accounts accounts = findAccounts(accountNumber);
        //-- 2. Find the account by accountNumber
        String mobileNumber = updateAccountDto.getMobileNumber();
        String idNumber  = updateAccountDto.getIdNumber();
        String branchAddress  = updateAccountDto.getBranchAddress();
        //-- If one of the customer unique attributes exists, it becomes an unprocessable entity
        isExist(idNumber, mobileNumber);
        //-- update the new attributes
        accounts.setMobileNumber(mobileNumber);
        accounts.setIdNumber(idNumber);
        accounts.setBranchAddress(branchAddress);
        //-- update the accounts object
        accountsRepository.save(accounts);
        //-- 3. return a proper message
        return new ResponseDto(AccountsConstants.MESSAGE_200,AccountsConstants.STATUS_200);
    }
    /**
     *
     * @param accountNumber
     * @return
     */
    @Override
    public ResponseDto deleteAccountByAccNumber(Long accountNumber) {
        //-- 1. Find the account by accountNumber
        Accounts accounts = findAccounts(accountNumber);
        //-- update the new attributes
        accounts.setBalance(BigDecimal.ZERO);
        accounts.setActiveSw(false);
        //-- update the accounts object
        accountsRepository.save(accounts);
        //-- 3. return a proper message
        return new ResponseDto(AccountsConstants.MESSAGE_204,AccountsConstants.STATUS_204);
    }
    /**
     * Check whether the Account with unique fields - ID Number, Mobile Number - already exists
     * @param idNumber
     * @param mobileNumber
     * @return
     */
    private void isExist(String idNumber, String mobileNumber) {
        boolean isExistByIdNumber = accountsRepository.existsByIdNumber(idNumber);
        boolean isExistByMobileNumber = accountsRepository.existsByMobileNumber(mobileNumber);
        //-- ID number exists
        if(isExistByIdNumber)
            throw new ResourceAlreadyExistsException("Account","ID Number",idNumber);
        //-- check if mobile Number exists
        if(isExistByMobileNumber)
            throw new ResourceAlreadyExistsException("Account","Mobile Number",mobileNumber);
    }
    /**
     *
     * @param account
     * @return
     */
    @Override
    public ResponseDto saveAccount(Accounts account) {
        accountsRepository.save(account);
        return new ResponseDto(AccountsConstants.MESSAGE_200, AccountsConstants.STATUS_200);
    }
    /**
     * Finds Accounts by Account Number
     * @param accountNumber
     * @return
     */
    @Override
    public Accounts findAccounts(Long accountNumber){
       return  accountsRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Customer Account","Account Number",String.valueOf(accountNumber)));
    }
}
