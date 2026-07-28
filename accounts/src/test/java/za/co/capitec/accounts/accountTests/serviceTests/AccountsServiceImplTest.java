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
import za.co.capitec.coreapi.exceptions.ResourceAlreadyExistsException;
import za.co.capitec.coreapi.exceptions.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    Accounts accountsOne, accountsTwo;
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
        //-- Keep the constructor injection under tbe setup
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

        accountsOne = buildAccount();
        accountsTwo = buildAccount2();
        createAccountsDto = createAccountDto();

    }

    @Test
    @DisplayName(value = "1. Should get all accounts by ID Number")
    void findAllAccountByIdNumber() {
        //-- Given
        List<Accounts> accounts = List.of(accountsOne, accountsTwo);
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
    @DisplayName(value = "2. Should return Accounts when find by AccountNumber")
    void shouldReturnAccountWhenFindByAccNumber() {
        //-- Given
        when(accountsRepository.findByAccountNumber(1001L)).thenReturn(Optional.of(accountsOne));
        //-- When
        AccountsRecord record = underTestService.findByAccNumber(1001L);

        //-- Then
        assertThat(record).isNotNull();
        assertThat(record.getAccountNumber()).isEqualTo(1001L);
        verify(accountsRepository, times(1)).findByAccountNumber(1001L);
        verifyNoMoreInteractions(accountsRepository);
    }
    /**
     * The
     */
    @Test
    @DisplayName(value = "3. Should create a Account when Account does not exist by accountNumber")
    void shouldCreateAccountWhenAccountDoesNotExistByAccountNumber() {
        //-- Given - See the create account request
        //-- When
        underTestService.createAccount(createAccountsDto);
        //-- Then
        verify(accountsRepository).save(accountsArgumentCaptor.capture());
        Accounts captured = accountsArgumentCaptor.getValue();
        //-- assert
        assertThat(captured.getAccountType()).isEqualTo(SAVINGS);
        assertThat(captured.getIdNumber()).isEqualTo(createAccountsDto.getIdNumber());
        assertThat(captured.getMobileNumber()).isEqualTo(createAccountsDto.getMobileNumber());
        assertThat(captured.getBranchAddress()).isEqualTo(createAccountsDto.getBranchAddress());
    }

    /**
     *
     */
    @Test
    @DisplayName(value = "4. Should not create an Account when Account exists by ID Number or Mobile Number")
    void shouldNotCreateAccountAndThrowExceptionWhenAccountExistsByIdNumber() {
        //-- Given - see the create request on setup
        //--When
        when(accountsRepository.existsByIdNumber(anyString())).thenReturn(Boolean.TRUE);
        //--
        assertThatThrownBy(() -> underTestService.createAccount(createAccountsDto))
                .isInstanceOf(ResourceAlreadyExistsException.class)
        .hasMessageContaining("Account with ID Number: "+createAccountsDto.getIdNumber()+" already exists");
        ;
    }

    /**
     *
     */
    @Test
    @DisplayName(value = "5. Should not create an Account when Account exists by Mobile Number")
    void shouldNotCreateAccountAndThrowExceptionWhenAccountExistsByMobileNumber() {
        //-- Given - see the create request on setup
        //--When
        when(accountsRepository.existsByMobileNumber(anyString())).thenReturn(Boolean.TRUE);
        //--
        assertThatThrownBy(() -> underTestService.createAccount(createAccountsDto))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("Account with Mobile Number: "+createAccountsDto.getMobileNumber()+" already exists");
    }

    @Test
    @DisplayName(value = "6. Should throw ResourceNotFoundException when given invalid Account Number while update Account")
    void shouldThrowResourceNotFoundExceptionWhenGivenInvalidAccNumberWhenUpdateAccount() {
        //-- Given - see the create request setup() method
        Long invalidAccNumber = 9999L;
        //-- When
        when(accountsRepository.findByAccountNumber(anyLong())).thenReturn(Optional.empty());
        //-- Then
        //-- assert
        assertThatThrownBy(() -> underTestService.updateAccountByAccNumber(invalidAccNumber, updateAccountDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Customer Account with Account Number: "+invalidAccNumber+" not found");
        verify(accountsRepository,never()).save(any());
    }

    @Test
    @DisplayName(value = "7. Should update only the Customer ID Number when given valid Account Number while update Account")
    void shouldUpdateOnlyTheCustomerIdNumberWhenGivenValidAccNumberWhenUpdateAccount() {
        //-- Given - see the create request setup() method
        String newIdNumber = AccountsUtilities.updatedIdNumber();
        UpdateAccountDto updateAccountDto = getUpdateAccountDtoIdNumberOnly();
        when(accountsRepository.findByAccountNumber(anyLong())).thenReturn(Optional.of(accountsOne));
        //-- When
        underTestService.updateAccountByAccNumber(1001L, updateAccountDto);
        //-- Then
        verify(accountsRepository).save(accountsArgumentCaptor.capture());
        //-- assert
        assertThat(accountsArgumentCaptor.getValue().getIdNumber()).isEqualTo(newIdNumber);
        assertThat(accountsArgumentCaptor.getValue().getMobileNumber()).isEqualTo(null);
    }

    @Test
    @DisplayName(value = "8. Should update only the Customer Mobile Number when given valid Account Number while update Account")
    void shouldUpdateOnlyTheCustomerMobileNumberWhenGivenValidAccNumberWhenUpdateAccount() {
        //-- Given - see the create request setup() method
        String newMobileNumber = AccountsUtilities.updatedContactNumber();
        UpdateAccountDto updateAccountDto = getUpdateAccountDtoMobileNumberOnly();
        when(accountsRepository.findByAccountNumber(anyLong())).thenReturn(Optional.of(accountsOne));
        //-- When
        underTestService.updateAccountByAccNumber(1001L, updateAccountDto);
        //-- Then
        verify(accountsRepository).save(accountsArgumentCaptor.capture());
        //-- assert
        assertThat(accountsArgumentCaptor.getValue().getIdNumber()).isEqualTo(null);
        assertThat(accountsArgumentCaptor.getValue().getMobileNumber()).isEqualTo(newMobileNumber);
    }

    @Test
    @DisplayName(value = "9. Should update only the Branch address  when given valid Account Number while update Account")
    void shouldUpdateOnlyTheBranchAddressWhenGivenValidAccNumberWhenUpdateAccount() {
        //-- Given - see the create request setup() method
        String newBranchAddress = "130 Botanic Gardens";
        UpdateAccountDto updateAccountDto = getUpdateAccountDtoAddressOnly();
        when(accountsRepository.findByAccountNumber(anyLong())).thenReturn(Optional.of(accountsOne));
        //-- When
        underTestService.updateAccountByAccNumber(1001L, updateAccountDto);
        //-- Then
        verify(accountsRepository).save(accountsArgumentCaptor.capture());
        //-- assert
        assertThat(accountsArgumentCaptor.getValue().getIdNumber()).isEqualTo(null);
        assertThat(accountsArgumentCaptor.getValue().getMobileNumber()).isEqualTo(null);
        assertThat(accountsArgumentCaptor.getValue().getBranchAddress()).isEqualTo(newBranchAddress);
    }

    @Test
    @DisplayName(value = "10. Should update all attributes when given valid Account Number while update Account")
    void shouldUpdateAllAttributesWhenGivenValidAccNumberWhenUpdateAccount() {
        //-- Given - see the create request setup() method
        String newBranchAddress = "130 Botanic Gardens";
        idNumber = AccountsUtilities.updatedIdNumber();
        mobileNumber = AccountsUtilities.updatedContactNumber();
        UpdateAccountDto updateAccountDto = getUpdateAccountDto();
        when(accountsRepository.findByAccountNumber(anyLong())).thenReturn(Optional.of(accountsOne));
        //-- When
        underTestService.updateAccountByAccNumber(1001L, updateAccountDto);
        //-- Then
        verify(accountsRepository).save(accountsArgumentCaptor.capture());
        //-- assert
        assertThat(accountsArgumentCaptor.getValue().getIdNumber()).isEqualTo(idNumber);
        assertThat(accountsArgumentCaptor.getValue().getMobileNumber()).isEqualTo(mobileNumber);
        assertThat(accountsArgumentCaptor.getValue().getBranchAddress()).isEqualTo(newBranchAddress);
    }

    @Test
    @DisplayName(value = "11. Should throw ResourceNotFoundException when given invalid Account Number while delete Account")
    void shouldThrowResourceNotFoundExceptionWhenGivenInvalidAccountNumberWhileDeleteAccount() {
        //-- Given - see the create request setup() method
        Long invalidAccountNumber = 9999L;
        //-- When
        when(accountsRepository.findByAccountNumber(anyLong())).thenReturn(Optional.empty());
        //-- Then
        //-- assert
        assertThatThrownBy(() -> underTestService.deleteAccountByAccNumber(invalidAccountNumber))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Customer Account with Account Number: "+invalidAccountNumber+" not found");
        verify(accountsRepository,never()).save(any());
    }

    @Test
    @DisplayName(value = "12. Should soft delete Accounts when given valid Account Number while delete Account")
    void shouldSoftDeleteAccountsWhenGivenValidAccountNumberWhileDeleteAccount() {
        //-- Given - see the create request setup() method
        Long validAccountNumber = 1001L;
        when(accountsRepository.findByAccountNumber(anyLong())).thenReturn(Optional.of(accountsOne));
        //-- When
        underTestService.deleteAccountByAccNumber(validAccountNumber);
        //-- Then
        verify(accountsRepository).save(accountsArgumentCaptor.capture());
        assertThat(accountsArgumentCaptor.getValue().getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(accountsArgumentCaptor.getValue().isActiveSw()).isFalse();
    }

    /**
     *
     * @return
     */
    Accounts buildAccount(){
        id = 1001L;
        accountNumber = 1001L;
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

    Accounts buildAccount2(){
        id = 1002L;
        accountNumber = 1002L;
        accountType = SAVINGS;
        balance = BigDecimal.ZERO;
        idNumber = AccountsUtilities.updatedIdNumber();
        mobileNumber = AccountsUtilities.updatedContactNumber();
        branchAddress = "130 Botanic Gardens";
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
    UpdateAccountDto getUpdateAccountDtoIdNumberOnly() {
        idNumber = AccountsUtilities.updatedIdNumber();
        return new UpdateAccountDto(null, idNumber, null);
    }
    /**
     * @return
     */
    UpdateAccountDto getUpdateAccountDtoMobileNumberOnly() {
        mobileNumber = AccountsUtilities.updatedContactNumber();
        return new UpdateAccountDto(mobileNumber, null, null);
    }
    /**
     *
     * @return
     */
    UpdateAccountDto getUpdateAccountDtoAddressOnly() {
        branchAddress = "130 Botanic Gardens";
        return new UpdateAccountDto(null, null, branchAddress);
    }
    /**
     *
     * @return
     */
    UpdateAccountDto getUpdateAccountDto() {
        idNumber = AccountsUtilities.updatedIdNumber();
        mobileNumber = AccountsUtilities.updatedContactNumber();
        branchAddress = "130 Botanic Gardens";
        return new UpdateAccountDto(mobileNumber, idNumber, branchAddress);
    }
}