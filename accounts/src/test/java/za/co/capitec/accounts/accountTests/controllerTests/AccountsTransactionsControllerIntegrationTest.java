package za.co.capitec.accounts.accountTests.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import za.co.capitec.accounts.Abstracts.AbstractContainersTest;
import za.co.capitec.accounts.entity.Accounts;
import za.co.capitec.accounts.entity.AccountsTransactions;
import za.co.capitec.accounts.repositories.AccountsRepository;
import za.co.capitec.accounts.repositories.TransactionRepository;
import za.co.capitec.accounts.utils.AccountsUtilities;
import za.co.capitec.accounts.utils.DateUtils;
import za.co.capitec.coreapi.dtos.ResponseDto;
import za.co.capitec.coreapi.dtos.accounts.requests.TransactionDto;
import za.co.capitec.coreapi.dtos.accounts.response.TransactionResponse;
import za.co.capitec.coreapi.enums.Categories;
import za.co.capitec.coreapi.enums.accounts.AccountType;
import za.co.capitec.coreapi.exceptions.ApiErrorResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static za.co.capitec.accounts.constants.AccountsConstants.MESSAGE_200;
import static za.co.capitec.accounts.constants.AccountsConstants.STATUS_200;
import static za.co.capitec.coreapi.enums.accounts.AccountType.SAVINGS;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.service-registry.auto-registration.enabled=false"
})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AccountsTransactionsControllerIntegrationTest extends AbstractContainersTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        accountsRepository.deleteAll();
    }

    @Test
    @DisplayName("1. Should debit account and persist transaction through controller")
    void shouldDebitAccountAndPersistTransactionThroughController() throws Exception {
        //-- Given
        Accounts savedAccount = accountsRepository.save(buildAccount(1001L, new BigDecimal("1000.00")));
        TransactionDto request = TransactionDto.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .description(Categories.DEBIT.getDescription())
                .amount(new BigDecimal("250.00"))
                .isImmediate(Boolean.TRUE)
                .category(Categories.DEBIT)
                .reference("ATM withdrawal")
                .build();

        //-- When
        MvcResult result = mockMvc.perform(post("/api/v1/transaction")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        //-- Then
        ResponseDto response = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseDto.class);
        Accounts reloadedAccount = accountsRepository.findByAccountNumber(savedAccount.getAccountNumber()).orElseThrow();
        List<AccountsTransactions> transactions = transactionRepository.findAll();

        assertThat(response.getStatusCode()).isEqualTo(MESSAGE_200);
        assertThat(response.getStatusMsg()).isEqualTo(STATUS_200);
        assertThat(reloadedAccount.getBalance()).isEqualByComparingTo("750.00");
        assertThat(transactions).hasSize(1);
        assertThat(transactions.getFirst().getCategory()).isEqualTo(Categories.DEBIT);
        assertThat(transactions.getFirst().getAmount()).isEqualByComparingTo("250.00");
        assertThat(transactions.getFirst().getAccounts().getId()).isEqualTo(savedAccount.getId());
    }

    @Test
    @DisplayName("2. Should credit savings account and persist interest transaction through controller")
    void shouldCreditSavingsAccountAndPersistInterestTransactionThroughController() throws Exception {
        //-- Given
        Accounts savedAccount = accountsRepository.save(buildAccount(1001L, new BigDecimal("1000.00")));
        TransactionDto request = TransactionDto.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .description(Categories.CREDIT.getDescription())
                .amount(new BigDecimal("1000.00"))
                .isImmediate(Boolean.TRUE)
                .category(Categories.CREDIT)
                .reference("Salary")
                .build();

        //-- When
        MvcResult result = mockMvc.perform(post("/api/v1/transaction")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        //-- Then
        ResponseDto response = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseDto.class);
        Accounts reloadedAccount = accountsRepository.findByAccountNumber(savedAccount.getAccountNumber()).orElseThrow();
        List<AccountsTransactions> transactions = transactionRepository.findAll();

        assertThat(response.getStatusCode()).isEqualTo(MESSAGE_200);
        assertThat(response.getStatusMsg()).isEqualTo(STATUS_200);
        assertThat(reloadedAccount.getBalance()).isEqualByComparingTo("2005.00");
        assertThat(transactions).hasSize(2);
        assertThat(transactions)
                .extracting(AccountsTransactions::getCategory)
                .containsExactlyInAnyOrder(Categories.CREDIT, Categories.INTEREST);
        assertThat(transactions)
                .filteredOn(tx -> tx.getCategory() == Categories.INTEREST)
                .singleElement()
                .satisfies(tx -> assertThat(tx.getAmount()).isEqualByComparingTo("5.00"));
    }

    @Test
    @DisplayName("3. Should return paginated account statement through controller")
    void shouldReturnPaginatedAccountStatementThroughController() throws Exception {
        //-- Given
        Accounts savedAccount = accountsRepository.save(buildAccount(1001L, new BigDecimal("3000.00")));
        transactionRepository.saveAll(List.of(
                buildTransaction(savedAccount, Categories.CREDIT, new BigDecimal("400.00"), LocalDate.of(2026, 7, 1), "Salary"),
                buildTransaction(savedAccount, Categories.DEBIT, new BigDecimal("150.00"), LocalDate.of(2026, 7, 10), "Groceries"),
                buildTransaction(savedAccount, Categories.DEBIT, new BigDecimal("50.00"), LocalDate.of(2026, 8, 1), "Outside range")
        ));

        //-- When
        MvcResult result = mockMvc.perform(get("/api/v1/transaction/{idNumber}/{startDate}/{endDate}",
                        savedAccount.getIdNumber(),
                        "2026-07-01",
                        "2026-07-31")
                        .param("pageNo", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "date")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andReturn();

        //-- Then
        TransactionResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), TransactionResponse.class);

        assertThat(response.getPageNo()).isZero();
        assertThat(response.getPageSize()).isEqualTo(10);
        assertThat(response.getTotalElements()).isEqualTo(2);
        assertThat(response.getContent()).hasSize(2);
        assertThat(response.getContent()).extracting(record -> record.getReference())
                .containsExactly("Salary", "Groceries");
        assertThat(response.getContent()).extracting(record -> record.getDate())
                .containsExactly(LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 10));
    }

    @Test
    @DisplayName("4. Should return unprocessable entity when debit amount exceeds balance")
    void shouldReturnUnprocessableEntityWhenDebitAmountExceedsBalance() throws Exception {
        //-- Given
        Accounts savedAccount = accountsRepository.save(buildAccount(1001L, new BigDecimal("1000.00")));
        TransactionDto request = TransactionDto.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .description(Categories.DEBIT.getDescription())
                .amount(new BigDecimal("1000.01"))
                .isImmediate(Boolean.TRUE)
                .category(Categories.DEBIT)
                .reference("Overspend")
                .build();

        //-- When
        MvcResult result = mockMvc.perform(post("/api/v1/transaction")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        //-- Then
        ApiErrorResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ApiErrorResponse.class);
        Accounts reloadedAccount = accountsRepository.findByAccountNumber(savedAccount.getAccountNumber()).orElseThrow();

        assertThat(response.getStatus().value()).isEqualTo(422);
        assertThat(response.getApi()).isEqualTo("handleTransact");
        assertThat(response.getPath()).isEqualTo("/api/v1/transaction");
        assertThat(response.getMessage()).isEqualTo("Insufficient funds!");
        assertThat(reloadedAccount.getBalance()).isEqualByComparingTo("1000.00");
        assertThat(transactionRepository.findAll()).isEmpty();
    }

    private Accounts buildAccount(Long accountNumber, BigDecimal balance) {
        return Accounts.builder()
                .accountNumber(accountNumber)
                .accountType(AccountType.SAVINGS)
                .balance(balance)
                .mobileNumber(AccountsUtilities.setContactNumber())
                .idNumber(AccountsUtilities.setIdNumber())
                .branchAddress("129 Botanic Gardens")
                .activeSw(true)
                .build();
    }

    private AccountsTransactions buildTransaction(Accounts account,
                                                  Categories category,
                                                  BigDecimal amount,
                                                  LocalDate date,
                                                  String reference) {
        return AccountsTransactions.builder()
                .accounts(account)
                .accountType(SAVINGS)
                .category(category)
                .description(category.getDescription())
                .amount(amount)
                .isImmediate(Boolean.TRUE)
                .reference(reference)
                .date(date)
                .time(DateUtils.getCurrentTime())
                .build();
    }
}
