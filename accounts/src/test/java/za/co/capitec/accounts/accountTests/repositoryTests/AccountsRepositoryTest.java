package za.co.capitec.accounts.accountTests.repositoryTests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import za.co.capitec.accounts.TestcontainersConfiguration;
import za.co.capitec.accounts.entity.Accounts;
import za.co.capitec.accounts.entity.AccountsTransactions;
import za.co.capitec.accounts.repositories.AccountsRepository;
import za.co.capitec.accounts.repositories.TransactionRepository;
import za.co.capitec.accounts.utilities.dates.DateUtils;
import za.co.capitec.accounts.utils.AccountsUtilities;
import za.co.capitec.coreapi.enums.Categories;
import za.co.capitec.coreapi.enums.accounts.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountsRepositoryTest {

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    TransactionRepository transactionRepository;

    Accounts account, savedAccount;
    AccountsTransactions transaction, savedTransaction;

    Long accountNumber;
    AccountType accountType; // SAVINGS, CHEQUE, Transactional
    BigDecimal balance;
    String mobileNumber;
    String idNumber;
    String branchAddress;
    boolean activeSw;

    Categories category;

    String description;
    BigDecimal amount;
    Boolean isImmediate;
    String reference;
    LocalTime time;
    LocalDate date;

    @BeforeEach
    void setUp() {
        //Given
        accountNumber = 1234567890L;
        accountType = AccountType.SAVINGS;
        balance = BigDecimal.valueOf(1000.00);
        mobileNumber = "0788298725";
        idNumber = "9202204720082";
        branchAddress = "129 Botanic Gardens";
        activeSw = true;
        account = Accounts.builder()
                .accountNumber(accountNumber)
                .accountType(accountType)
                .balance(balance)
                .mobileNumber(mobileNumber)
                .idNumber(idNumber)
                .branchAddress(branchAddress)
                .activeSw(activeSw)
                .build();
        log.info("Account before save: {}", account);
        accountType = AccountType.SAVINGS;
        category = Categories.DEBIT;
        description = "Test transaction";
        amount = BigDecimal.valueOf(100.00);
        isImmediate = true;
        reference = "Test reference";
        time = DateUtils.getCurrentTime();
        date = DateUtils.getCurrentDate();

        transaction = AccountsTransactions.builder()
                .accountType(accountType)
                .category(category)
                .description(description)
                .amount(amount)
                .isImmediate(isImmediate)
                .reference(reference)
                .time(time)
                .date(date)
                .build();

        savedAccount = accountsRepository.save(account);
        transaction.setAccounts(savedAccount);
        log.info("Testing transaction: {}", transaction);
        savedTransaction = transactionRepository.save(transaction);
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        accountsRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "1. should return an Account when find by Customer account number")
    void shouldReturnAccountWhenFindByCustomerAccountNumber() {
        //-- Given - see the setUp()

        //-- When
        Optional<Accounts> reloaded = accountsRepository.findByAccountNumber(accountNumber);

        //-- Then
        assertThat(reloaded).isPresent();
    }

    @Test
    @DisplayName(value = "2. Should return Account when find by ID number")
    void shouldReturnAccountWhenFindByIdNumber() {
        //Given - see the setUp() method

        //When
        List<Accounts> reloaded = accountsRepository.findAllByIdNumber(idNumber);
        //Then
        assertThat(reloaded).isNotEmpty();
    }

    @Test
    @DisplayName(value = "3. should not return Account when find by Customer account number is not present")
    void shouldNotReturnAccountWhenFindByCustomerAccountNumberIsNotPresent() {
        //-- Given
        Long invalidAccountNumber = AccountsUtilities.updatedAccountNumber();
        //-- When
        Optional<Accounts> reloaded = accountsRepository.findByAccountNumber(invalidAccountNumber);

        //-- Then
        assertThat(reloaded).isNotPresent();
    }
    @Test
    @DisplayName(value = "4. Should not return Account when find by ID number is not present")
    void shouldNotReturnAccountWhenFindByIdNumberIsNotPresent() {
        //Given - see the setUp() method
        String invalidIdNumber = AccountsUtilities.updatedIdNumber();
        //When
        List<Accounts> reloaded = accountsRepository.findAllByIdNumber(invalidIdNumber);
        //Then
        assertThat(reloaded).isEmpty();
    }
}
