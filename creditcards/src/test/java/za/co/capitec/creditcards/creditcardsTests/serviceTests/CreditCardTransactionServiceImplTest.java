package za.co.capitec.creditcards.creditcardsTests.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import za.co.capitec.creditcards.dtos.requests.CreditCardTransactionDto;
import za.co.capitec.creditcards.dtos.response.CreditCardTransactionResponse;
import za.co.capitec.creditcards.dtos.response.ResponseDto;
import za.co.capitec.creditcards.entity.CreditCardTransactions;
import za.co.capitec.creditcards.entity.CreditCards;
import za.co.capitec.creditcards.enums.Categories;
import za.co.capitec.creditcards.enums.CreditCardType;
import za.co.capitec.creditcards.exceptions.InsufficientFundsException;
import za.co.capitec.creditcards.repositories.CreditCardTransactionRepository;
import za.co.capitec.creditcards.services.ICreditCardService;
import za.co.capitec.creditcards.services.impl.CreditCardTransactionServiceImpl;
import za.co.capitec.creditcards.utils.CreditCardsUtilities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditCardTransactionServiceImplTest {

    private CreditCardTransactionServiceImpl underTestService;

    @Mock
    private CreditCardTransactionRepository transactionRepository;

    @Mock
    private ICreditCardService creditCardService;

    @Captor
    private ArgumentCaptor<CreditCardTransactions> transactionCaptor;

    private CreditCards creditCard;

    @BeforeEach
    void setUp() {
        underTestService = new CreditCardTransactionServiceImpl(transactionRepository, creditCardService);

        creditCard = CreditCards.builder()
                .id(1L)
                .cardNumber(CreditCardsUtilities.setCardNumber())
                .accountNumber(4455667788L)
                .cardType(CreditCardType.VISA)
                .mobileNumber(CreditCardsUtilities.setContactNumber())
                .idNumber(CreditCardsUtilities.setIdNumber())
                .amount(new BigDecimal("1000.00"))
                .creditLimit(new BigDecimal("5000.00"))
                .availableCredit(new BigDecimal("4000.00"))
                .outstandingBalance(new BigDecimal("1000.00"))
                .minimumPayment(new BigDecimal("100.00"))
                .issueDate(LocalDate.now().minusMonths(1))
                .expiryDate(LocalDate.now().plusYears(2))
                .activeSw(true)
                .build();
    }

    @Test
    @DisplayName("1. should return credit card transactions by id number and date range")
    void shouldReturnCreditCardTransactionsByIdNumberAndDateRange() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "date";
        String sortDir = "asc";
        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        CreditCardTransactions tx = CreditCardTransactions.create(
                creditCard,
                "Card purchase",
                new BigDecimal("20.00"),
                Categories.DEBIT);
        Page<CreditCardTransactions> page = new PageImpl<>(List.of(tx), pageable, 1);

        when(transactionRepository.findByCreditCardIdNumberAndDateBetween(CreditCardsUtilities.setIdNumber(), pageable, from, to))
                .thenReturn(page);

        CreditCardTransactionResponse response = underTestService.findAllByIdNumber(
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                CreditCardsUtilities.setIdNumber(),
                from,
                to);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        verify(transactionRepository)
                .findByCreditCardIdNumberAndDateBetween(CreditCardsUtilities.setIdNumber(), pageable, from, to);
    }

    @Test
    @DisplayName("2. should credit card on CREDIT transaction")
    void shouldCreditCardOnCreditTransaction() {
        CreditCardTransactionDto dto = CreditCardTransactionDto.builder()
                .cardNumber(creditCard.getCardNumber())
                .description("Salary")
                .amount(new BigDecimal("300.00"))
                .category(Categories.CREDIT)
                .build();

        when(creditCardService.findCreditCard(creditCard.getCardNumber())).thenReturn(creditCard);
        when(creditCardService.saveCreditCard(creditCard)).thenReturn(new ResponseDto("200", "OK"));

        underTestService.transact(dto);

        verify(transactionRepository).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue().getCategory()).isEqualTo(Categories.CREDIT);
        assertThat(creditCard.getAmount()).isEqualByComparingTo("1300.00");
    }

    @Test
    @DisplayName("3. should debit card on DEBIT transaction")
    void shouldDebitCardOnDebitTransaction() {
        CreditCardTransactionDto dto = CreditCardTransactionDto.builder()
                .cardNumber(creditCard.getCardNumber())
                .description("Groceries")
                .amount(new BigDecimal("100.00"))
                .category(Categories.DEBIT)
                .build();

        when(creditCardService.findCreditCard(creditCard.getCardNumber())).thenReturn(creditCard);
        when(creditCardService.saveCreditCard(creditCard)).thenReturn(new ResponseDto("200", "OK"));

        underTestService.transact(dto);

        verify(transactionRepository).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue().getCategory()).isEqualTo(Categories.DEBIT);
        assertThat(creditCard.getAmount()).isEqualByComparingTo("900.00");
    }

    @Test
    @DisplayName("4. should throw on debit with insufficient funds")
    void shouldThrowOnDebitWithInsufficientFunds() {
        CreditCardTransactionDto dto = CreditCardTransactionDto.builder()
                .cardNumber(creditCard.getCardNumber())
                .description("Overdraft")
                .amount(new BigDecimal("5000.00"))
                .category(Categories.DEBIT)
                .build();

        when(creditCardService.findCreditCard(creditCard.getCardNumber())).thenReturn(creditCard);

        assertThatThrownBy(() -> underTestService.transact(dto))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessageContaining("Insufficient funds");

        verify(transactionRepository, never()).save(any());
    }
}

