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
import za.co.capitec.accounts.repositories.AccountsRepository;
import za.co.capitec.accounts.repositories.TransactionRepository;
import za.co.capitec.accounts.utils.AccountsUtilities;
import za.co.capitec.coreapi.dtos.ResponseDto;
import za.co.capitec.coreapi.dtos.accounts.records.AccountsRecord;
import za.co.capitec.coreapi.dtos.accounts.requests.CreateAccountsDto;
import za.co.capitec.coreapi.dtos.accounts.requests.UpdateAccountDto;
import za.co.capitec.coreapi.enums.accounts.AccountType;
import za.co.capitec.coreapi.exceptions.ApiErrorResponse;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static za.co.capitec.accounts.constants.AccountsConstants.MESSAGE_200;
import static za.co.capitec.accounts.constants.AccountsConstants.MESSAGE_201;
import static za.co.capitec.accounts.constants.AccountsConstants.STATUS_200;
import static za.co.capitec.accounts.constants.AccountsConstants.STATUS_201;
import static za.co.capitec.coreapi.enums.accounts.AccountType.SAVINGS;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.service-registry.auto-registration.enabled=false"
})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AccountsControllerIntegrationTest extends AbstractContainersTest {

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
    @DisplayName("1. Should create account through Accounts controller")
    void shouldCreateAccountThroughAccountsController() throws Exception {
        //-- Given
        CreateAccountsDto request = new CreateAccountsDto(
                SAVINGS.name(),
                AccountsUtilities.setContactNumber(),
                AccountsUtilities.setIdNumber(),
                "129 Botanic Gardens"
        );

        //-- When
        MvcResult result = mockMvc.perform(post("/api/v1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        //-- Then
        ResponseDto response = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseDto.class);
        List<Accounts> savedAccounts = accountsRepository.findAll();

        assertThat(response.getStatusCode()).isEqualTo(MESSAGE_201);
        assertThat(response.getStatusMsg()).isEqualTo(STATUS_201);
        assertThat(savedAccounts).hasSize(1);

        Accounts savedAccount = savedAccounts.getFirst();
        assertThat(savedAccount.getAccountNumber()).isNotNull();
        assertThat(savedAccount.getAccountType()).isEqualTo(SAVINGS);
        assertThat(savedAccount.getMobileNumber()).isEqualTo(request.getMobileNumber());
        assertThat(savedAccount.getIdNumber()).isEqualTo(request.getIdNumber());
        assertThat(savedAccount.getBranchAddress()).isEqualTo(request.getBranchAddress());
        assertThat(savedAccount.isActiveSw()).isTrue();
    }

    @Test
    @DisplayName("2. Should return account by account number through Accounts controller")
    void shouldReturnAccountByAccountNumberThroughAccountsController() throws Exception {
        //-- Given
        Accounts savedAccount = accountsRepository.save(buildAccount(1001L, AccountsUtilities.setIdNumber(), AccountsUtilities.setContactNumber(), "129 Botanic Gardens", true, new BigDecimal("1000.00")));

        //-- When
        MvcResult result = mockMvc.perform(get("/api/v1/{accountNumber}", savedAccount.getAccountNumber()))
                .andExpect(status().isOk())
                .andReturn();

        //-- Then
        AccountsRecord response = objectMapper.readValue(result.getResponse().getContentAsString(), AccountsRecord.class);
        assertThat(response.getAccountNumber()).isEqualTo(savedAccount.getAccountNumber());
        assertThat(response.getAccountType()).isEqualTo(savedAccount.getAccountType());
        assertThat(response.getMobileNumber()).isEqualTo(savedAccount.getMobileNumber());
        assertThat(response.getIdNumber()).isEqualTo(savedAccount.getIdNumber());
        assertThat(response.getBranchAddress()).isEqualTo(savedAccount.getBranchAddress());
        assertThat(response.isActiveSw()).isEqualTo(savedAccount.isActiveSw());
    }

    @Test
    @DisplayName("3. Should return active accounts by ID number through Accounts controller")
    void shouldReturnActiveAccountsByIdNumberThroughAccountsController() throws Exception {
        //-- Given
        String idNumber = AccountsUtilities.setIdNumber();
        accountsRepository.save(buildAccount(1001L, idNumber, AccountsUtilities.setContactNumber(), "129 Botanic Gardens", true, new BigDecimal("1000.00")));
        accountsRepository.save(buildAccount(1002L, idNumber, AccountsUtilities.updatedContactNumber(), "130 Botanic Gardens", false, BigDecimal.ZERO));

        //-- When
        MvcResult result = mockMvc.perform(get("/api/v1/accounts")
                        .param("idNumber", idNumber))
                .andExpect(status().isOk())
                .andReturn();

        //-- Then
        AccountsRecord[] response = objectMapper.readValue(result.getResponse().getContentAsString(), AccountsRecord[].class);
        assertThat(response).hasSize(1);
        assertThat(response[0].getAccountNumber()).isEqualTo(1001L);
        assertThat(response[0].isActiveSw()).isTrue();
    }

    @Test
    @DisplayName("4. Should update account by account number through Accounts controller")
    void shouldUpdateAccountByAccountNumberThroughAccountsController() throws Exception {
        //-- Given
        Long accountNumber = 1001L;
        accountsRepository.save(buildAccount(accountNumber, AccountsUtilities.setIdNumber(), AccountsUtilities.setContactNumber(), "129 Botanic Gardens", true, BigDecimal.ZERO));
        UpdateAccountDto request = new UpdateAccountDto(
                AccountsUtilities.updatedContactNumber(),
                AccountsUtilities.updatedIdNumber(),
                "130 Botanic Gardens"
        );

        //-- When
        MvcResult result = mockMvc.perform(put("/api/v1/{accountNumber}", accountNumber)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        //-- Then
        ResponseDto response = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseDto.class);
        Accounts updatedAccount = accountsRepository.findByAccountNumber(accountNumber).orElseThrow();

        assertThat(response.getStatusCode()).isEqualTo(MESSAGE_200);
        assertThat(response.getStatusMsg()).isEqualTo(STATUS_200);
        assertThat(updatedAccount.getMobileNumber()).isEqualTo(request.getMobileNumber());
        assertThat(updatedAccount.getIdNumber()).isEqualTo(request.getIdNumber());
        assertThat(updatedAccount.getBranchAddress()).isEqualTo(request.getBranchAddress());
    }

    @Test
    @DisplayName("5. Should soft delete account through Accounts controller")
    void shouldSoftDeleteAccountThroughAccountsController() throws Exception {
        //-- Given
        Long accountNumber = 1001L;
        accountsRepository.save(buildAccount(accountNumber, AccountsUtilities.setIdNumber(), AccountsUtilities.setContactNumber(), "129 Botanic Gardens", true, new BigDecimal("2500.00")));

        //-- When
        mockMvc.perform(delete("/api/v1/{accountNumber}", accountNumber))
                .andExpect(status().isNoContent());

        //-- Then
        Accounts deletedAccount = accountsRepository.findByAccountNumber(accountNumber).orElseThrow();
        assertThat(deletedAccount.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(deletedAccount.isActiveSw()).isFalse();
    }

    @Test
    @DisplayName("6. Should return not found when account number does not exist")
    void shouldReturnNotFoundWhenAccountNumberDoesNotExist() throws Exception {
        //-- Given
        Long invalidAccountNumber = 9999L;

        //-- When
        MvcResult result = mockMvc.perform(get("/api/v1/{accountNumber}", invalidAccountNumber))
                .andExpect(status().isNotFound())
                .andReturn();

        //-- Then
        ApiErrorResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ApiErrorResponse.class);
        assertThat(response.getStatus().value()).isEqualTo(404);
        assertThat(response.getPath()).isEqualTo("/api/v1/9999");
        assertThat(response.getApi()).isEqualTo("handleFindByAccNumber");
        assertThat(response.getMessage()).contains("Customer Account with Account Number: 9999 not found");
    }

    @Test
    @DisplayName("7. Should patch account by account number through Accounts controller")
    void shouldPatchAccountByAccountNumberThroughAccountsController() throws Exception {
        //-- Given
        Long accountNumber = 1001L;
        accountsRepository.save(buildAccount(accountNumber, AccountsUtilities.setIdNumber(), AccountsUtilities.setContactNumber(), "129 Botanic Gardens", true, BigDecimal.ZERO));
        UpdateAccountDto request = new UpdateAccountDto(null, null, "130 Botanic Gardens");

        //-- When
        MvcResult result = mockMvc.perform(patch("/api/v1/{accountNumber}", accountNumber)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        //-- Then
        ResponseDto response = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseDto.class);
        Accounts updatedAccount = accountsRepository.findByAccountNumber(accountNumber).orElseThrow();

        assertThat(response.getStatusCode()).isEqualTo(MESSAGE_200);
        assertThat(response.getStatusMsg()).isEqualTo(STATUS_200);
        assertThat(updatedAccount.getBranchAddress()).isEqualTo(request.getBranchAddress());
    }

    private Accounts buildAccount(Long accountNumber,
                                  String idNumber,
                                  String mobileNumber,
                                  String branchAddress,
                                  boolean activeSw,
                                  BigDecimal balance) {
        return Accounts.builder()
                .accountNumber(accountNumber)
                .accountType(AccountType.SAVINGS)
                .balance(balance)
                .mobileNumber(mobileNumber)
                .idNumber(idNumber)
                .branchAddress(branchAddress)
                .activeSw(activeSw)
                .build();
    }
}
