package za.co.capitec.accounts.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import za.co.capitec.accounts.entity.Accounts;
import za.co.capitec.accounts.repositories.AccountsRepository;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AccountsServiceImplTest {

    AccountsServiceImpl accountsService;

    @Mock
    AccountsRepository accountsRepository;

    @Capture
    ArgumentCaptor<Accounts> accountsArgumentCaptor;

    @Test
    void createAccount() {
    }

    @Test
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
}