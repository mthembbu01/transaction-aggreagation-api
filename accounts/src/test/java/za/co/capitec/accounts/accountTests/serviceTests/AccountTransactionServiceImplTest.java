package za.co.capitec.accounts.accountTests.serviceTests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import za.co.capitec.accounts.entity.Accounts;
import za.co.capitec.accounts.entity.AccountsTransactions;
import za.co.capitec.accounts.repositories.TransactionRepository;
import za.co.capitec.accounts.services.IAccountService;
import za.co.capitec.accounts.services.impl.AccountTransactionServiceImpl;
import za.co.capitec.accounts.utils.AccountsUtilities;
import za.co.capitec.accounts.utils.DateUtils;
import za.co.capitec.coreapi.dtos.ResponseDto;
import za.co.capitec.coreapi.dtos.accounts.requests.TransactionDto;
import za.co.capitec.coreapi.dtos.accounts.response.TransactionResponse;
import za.co.capitec.coreapi.enums.Categories;
import za.co.capitec.coreapi.enums.accounts.AccountType;
import za.co.capitec.coreapi.exceptions.InsufficientFundsException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static za.co.capitec.coreapi.enums.accounts.AccountType.SAVINGS;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AccountTransactionServiceImplTest {

    AccountTransactionServiceImpl underTestService;

    @Mock
    IAccountService accountService;

    @Mock
    TransactionRepository transactionRepository;

    @Captor
    ArgumentCaptor<AccountsTransactions> transactionsArgumentCaptor;

    TransactionDto  transactionDto;

    Long id;
    Long accountNumber;
    Categories category;
    BigDecimal balance;
    BigDecimal amount;
    String idNumber;
    String mobileNumber;
    String description;
    Boolean isImmediate;
    LocalDate dateFrom, dateTo;
    LocalTime time;
    String reference;

    Accounts accountsOne;
    AccountType accountType; // SAVINGS, CHEQUE, Transactional
    String branchAddress;
    boolean activeSw;

    int pageNo, pageSize;
    String sortBy, sortDir;

    @BeforeEach
    void setUp() {
        //-- Keep the constructor injection under the setup
        underTestService = new AccountTransactionServiceImpl(transactionRepository, accountService);
        //--
        pageNo = 0;
        pageSize = 10;
        sortBy = "date";
        sortDir = "asc";
        dateFrom = DateUtils.getCurrentDate().minusDays(30);
        dateTo = DateUtils.getCurrentDate();
        accountsOne = buildAccount();
        transactionDto = buildDebitTransactionsDto();
    }

    @AfterEach
    void tearDown() {
        underTestService = null;
        transactionRepository.deleteAll();
    }

    @Test
            @DisplayName(value = "1. Should return all Transactions by pagination frm startDate to endDate for a given IdNumber")
    void shouldReturnAllTransactionsByPaginationFrmStartDateToEndDateForValidIdNumber() {
        //--Given
        Sort sort = sortDirection(sortBy, sortDir);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        //-- Create a mock Page with sample transaction data
        List<AccountsTransactions> transactionsList = List.of(); // Empty list for this test
        Page<AccountsTransactions> mockPage = new PageImpl<>(transactionsList, pageable, 0);
        //-- Setup mock repository to return the expected page
        when(transactionRepository.findByAccountsIdNumberAndDateBetween(
                pageable, accountsOne.getIdNumber(), dateFrom, dateTo))
                .thenReturn(mockPage);
        //--When
        TransactionResponse actualResponse = underTestService.findAllByIdNumber(
                pageNo, pageSize, sortBy, sortDir, accountsOne.getIdNumber(), dateFrom, dateTo);
        //--Then
        //-- assert
        assertThat(actualResponse).isNotNull();
        verify(transactionRepository).findByAccountsIdNumberAndDateBetween(pageable, accountsOne.getIdNumber(), dateFrom, dateTo);
    }

    @Test
    @DisplayName(value = "2. Should DEBIT account when transaction is DEBIT and sufficient funds are available")
    void shouldDebitAccountWhenTransactionIsDebitAndSufficientFundsAreAvailable() {
        //-- Given
        when(accountService.saveAccount(accountsOne)).thenReturn(mock(ResponseDto.class));
        when(accountService.findAccounts(accountsOne.getAccountNumber())).thenReturn(accountsOne);

        transactionDto = buildDebitTransactionsDto();
        //-- When
        underTestService.transact(transactionDto);
        //-- Then
        verify(transactionRepository).save(transactionsArgumentCaptor.capture());
        AccountsTransactions capturedTransaction = transactionsArgumentCaptor.getValue();
        //-- assert
        assertThat(capturedTransaction).isNotNull();
        assertThat(capturedTransaction.getAmount()).isEqualByComparingTo(transactionDto.getAmount());
        assertThat(accountsOne.getBalance()).isEqualByComparingTo("0.00");

    }

    @Test
    @DisplayName(value = "3. Should CREDIT account when transaction is CREDIT and sufficient funds are available")
    void shouldCreditAccountWhenTransactionIsCreditAndSufficientFundsAreAvailable() {
        //-- Given
        when(accountService.saveAccount(accountsOne)).thenReturn(mock(ResponseDto.class));
        when(accountService.findAccounts(accountsOne.getAccountNumber())).thenReturn(accountsOne);

        transactionDto = buildCreditTransactionsDto();
        //-- When
        underTestService.transact(transactionDto);
        //-- Then
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<AccountsTransactions>> transactionsListCaptor = ArgumentCaptor.forClass(List.class);
        verify(transactionRepository).saveAll(transactionsListCaptor.capture());
        List<AccountsTransactions> capturedTransactions = transactionsListCaptor.getValue();
        //-- assert
        assertThat(capturedTransactions).isNotNull();
        assertThat(capturedTransactions)
                .hasSize(2)
                .extracting(AccountsTransactions::getCategory)
                .containsExactlyInAnyOrder(
                        Categories.CREDIT,
                        Categories.INTEREST
                );

        assertThat(capturedTransactions)
                .filteredOn(tx -> tx.getCategory() == Categories.CREDIT)
                .singleElement()
                .extracting(AccountsTransactions::getAmount)
                .isEqualTo(transactionDto.getAmount());

        assertThat(capturedTransactions)
                .filteredOn(tx -> tx.getCategory() == Categories.INTEREST)
                .singleElement()
                .extracting(AccountsTransactions::getAmount)
                .isEqualTo(new BigDecimal("5.00000"));
        assertThat(accountsOne.getBalance()).isEqualByComparingTo("2005.00");

    }

    @Test
    @DisplayName(value = "4. Should throw Insufficient funds exception when transaction amount exceeds account balance")
    void shouldThrowInsufficientFundsExceptionWhenTransactionAmountExceedsAccountBalance() {
        //-- Given
        when(accountService.findAccounts(accountsOne.getAccountNumber())).thenReturn(accountsOne);
        transactionDto = buildDebitTransactionsDto();
        transactionDto.setAmount(new BigDecimal("1000.01"));

        //-- Then
        assertThatThrownBy(() -> underTestService.transact(transactionDto))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("Insufficient funds!");
        verify(transactionRepository, never()).save(any());
        verify(accountService, never()).saveAccount(any());
    }

    /**
     *
     * @return
     */
    Accounts buildAccount(){
        id = 1001L;
        accountNumber = 1001L;
        accountType = SAVINGS;
        balance = new BigDecimal("1000.00");
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
    TransactionDto buildDebitTransactionsDto() {
        accountNumber = 1001L;
        description = Categories.DEBIT.getDescription();
        category = Categories.DEBIT;
        amount = new BigDecimal("1000.01");
        isImmediate = Boolean.TRUE;
        reference = "Test DEBIT Transaction";

        return TransactionDto.builder()
                .accountNumber(accountNumber)
                .description(description)
                .amount(balance)
                .isImmediate(isImmediate)
                .category(category)
                .build();
    }

    /**
     *
     * @return
     */
    TransactionDto buildCreditTransactionsDto() {
        accountNumber = 1001L;
        description = Categories.CREDIT.getDescription();
        category = Categories.CREDIT;
        amount = new BigDecimal("1000.00");
        isImmediate = Boolean.TRUE;
        reference = "Test CREDIT Transaction";

        return TransactionDto.builder()
                .accountNumber(accountNumber)
                .description(description)
                .amount(balance)
                .isImmediate(isImmediate)
                .category(category)
                .build();
    }

    /**
     *
     * @param sortBy
     * @param sortDir
     * @return
     */
    Sort sortDirection(String sortBy, String sortDir) {
        return sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
    }
}
