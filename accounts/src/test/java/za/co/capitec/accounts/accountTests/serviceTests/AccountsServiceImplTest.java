package za.co.capitec.accounts.accountTests.serviceTests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import za.co.capitec.accounts.entity.Accounts;
import za.co.capitec.accounts.repositories.AccountsRepository;
import za.co.capitec.accounts.services.impl.AccountsServiceImpl;
import za.co.capitec.accounts.utils.AccountsUtilities;
import za.co.capitec.coreapi.dtos.accounts.records.AccountsRecord;
import za.co.capitec.coreapi.dtos.accounts.requests.CreateAccountsDto;
import za.co.capitec.coreapi.dtos.accounts.requests.UpdateAccountDto;
import za.co.capitec.coreapi.enums.accounts.AccountType;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static za.co.capitec.coreapi.enums.accounts.AccountType.SAVINGS;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AccountsServiceImplTest {

    AccountsServiceImpl underTestService;
    ModelMapper modelMapper;

    @Mock
    AccountsRepository accountsRepository;

    @Captor
    ArgumentCaptor<Accounts> accountsArgumentCaptor;

    Accounts accounts, savedAccounts;
    CreateAccountsDto createAccountsDto;
    UpdateAccountDto updateAccountDto;

    Long accountNumber, id;
    AccountType accountType; // SAVINGS, CHEQUE, Transactional
    BigDecimal balance;
    String mobileNumber;
    String idNumber;
    String branchAddress;
    boolean activeSw;

    @BeforeEach
    void setUp() {
        //-- Keep the constructor injection under tbe
        modelMapper = new ModelMapper();
        underTestService = new AccountsServiceImpl(accountsRepository, modelMapper);

        //-- Given
        id = 1L;
        accountNumber = AccountsUtilities.setAccountNumber();
        accountType = SAVINGS;
        balance = BigDecimal.ZERO;
        idNumber = AccountsUtilities.setIdNumber();
        mobileNumber = AccountsUtilities.setContactNumber();
        branchAddress = "129 Botanic Gardens";
        activeSw = true;

        accounts = savedAccounts;
        createAccountsDto = createAccountDto();

    }


    @Test
    @DisplayName(value = "1. Should get all accounts by ID Number")
    void findAllAccount() {
        //-- Given
        List<Accounts> accounts = List.of(Accounts.builder()
                .accountNumber(1001L)
                .accountType(SAVINGS)
                .balance(BigDecimal.ZERO)
                .idNumber(idNumber)
                .mobileNumber(mobileNumber)
                .branchAddress(branchAddress)
                .activeSw(activeSw)
                .build(),
                Accounts.builder()
                        .accountNumber(1002L)
                        .accountType(SAVINGS)
                        .balance(BigDecimal.ZERO)
                        .idNumber(idNumber)
                        .mobileNumber(mobileNumber)
                        .branchAddress(branchAddress)
                        .activeSw(activeSw)
                        .build());
        when(accountsRepository.existsByIdNumber(idNumber)).thenReturn(true);
        when(accountsRepository.findAllByIdNumber(idNumber)).thenReturn(accounts);
        //-- When
        List<AccountsRecord> records = underTestService.findAccountsByIdNumber(idNumber);
        //--Then
        assertThat(records).hasSize(2);
        assertThat(records).extracting(AccountsRecord::getAccountNumber)
                .containsExactly(1001L, 1002L);

        verify(accountsRepository, times(1)).existsByIdNumber(idNumber);
        verify(accountsRepository, times(1)).findAllByIdNumber(idNumber);
        verifyNoMoreInteractions(accountsRepository);

    }

    @Test
    @DisplayName(value = "2. Should find account by account number")
    void findByAccNumber() {
    }

    @Test
    void findAccountsByIdNumber() {
    }

    @Test
    void updateAccountByAccNumber() {
    }

    @Test
    void deleteAccountByAccNumber() {
    }

    @Test
    void saveAccount() {
    }

    @Test
    void findAccounts() {
    }

    /**
     *
     * @return
     */
    Accounts savedAccount(){
        id = 1L;
        accountNumber = AccountsUtilities.setAccountNumber();
        accountType = SAVINGS;
        balance = BigDecimal.ZERO;
        idNumber = AccountsUtilities.setIdNumber();
        mobileNumber = AccountsUtilities.setContactNumber();
        branchAddress = "129 Botanic Gardens";
        activeSw = true;

        return Accounts.builder()
                .id(id)
                .accountNumber(accountNumber)
                .accountType(accountType)
                .balance(balance)
                .idNumber(idNumber)
                .mobileNumber(mobileNumber)
                .branchAddress(branchAddress)
                .activeSw(activeSw)
                .build();
    }

    /**
     *
     * @return
     */
    CreateAccountsDto createAccountDto(){
        idNumber = AccountsUtilities.setIdNumber();
        mobileNumber = AccountsUtilities.setContactNumber();
        branchAddress = "129 Botanic Gardens";
        return new CreateAccountsDto("SAVINGS",mobileNumber,idNumber,branchAddress);
    }

    /**
     *
     * @return
     */
    UpdateAccountDto getUpdateAccountDto() {
        idNumber = AccountsUtilities.setIdNumber();
        mobileNumber = AccountsUtilities.setContactNumber();
        branchAddress = "129 Botanic Gardens";
        return new UpdateAccountDto(mobileNumber, idNumber, branchAddress);
    }
}