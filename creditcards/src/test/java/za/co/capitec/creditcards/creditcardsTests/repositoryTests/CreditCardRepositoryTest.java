package za.co.capitec.creditcards.creditcardsTests.repositoryTests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import za.co.capitec.creditcards.Abstracts.AbstractContainersTest;
import za.co.capitec.creditcards.entity.CreditCardTransactions;
import za.co.capitec.creditcards.entity.CreditCards;
import za.co.capitec.creditcards.enums.Categories;
import za.co.capitec.creditcards.enums.CreditCardType;
import za.co.capitec.creditcards.repositories.CreditCardRepository;
import za.co.capitec.creditcards.repositories.CreditCardTransactionRepository;
import za.co.capitec.creditcards.utils.CreditCardsUtilities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditCardRepositoryTest extends AbstractContainersTest {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CreditCardTransactionRepository creditCardTransactionRepository;

    private CreditCards savedCreditCard;
    private Long cardNumber;
    private String idNumber;

    @BeforeEach
    void setUp() {
        cardNumber = CreditCardsUtilities.setCardNumber();
        idNumber = CreditCardsUtilities.setIdNumber();

        CreditCards card = CreditCards.builder()
                .cardNumber(cardNumber)
                .accountNumber(4455667788L)
                .cardType(CreditCardType.VISA)
                .mobileNumber(CreditCardsUtilities.setContactNumber())
                .idNumber(idNumber)
                .amount(new BigDecimal("1000.00"))
                .creditLimit(new BigDecimal("5000.00"))
                .availableCredit(new BigDecimal("4000.00"))
                .outstandingBalance(new BigDecimal("1000.00"))
                .minimumPayment(new BigDecimal("100.00"))
                .issueDate(LocalDate.now().minusMonths(2))
                .expiryDate(LocalDate.now().plusYears(3))
                .activeSw(true)
                .build();

        savedCreditCard = creditCardRepository.save(card);

        CreditCardTransactions transaction = CreditCardTransactions.builder()
                .creditCard(savedCreditCard)
                .cardType(CreditCardType.VISA)
                .category(Categories.DEBIT)
                .description("Card test transaction")
                .amount(new BigDecimal("150.00"))
                .date(LocalDate.now())
                .build();

        creditCardTransactionRepository.save(transaction);
        log.info("Saved card id={}, cardNumber={}", savedCreditCard.getId(), savedCreditCard.getCardNumber());
    }

    @AfterEach
    void tearDown() {
        creditCardTransactionRepository.deleteAll();
        creditCardRepository.deleteAll();
    }

    @Test
    @DisplayName("1. should return credit card when find by card number")
    void shouldReturnCreditCardWhenFindByCardNumber() {
        Optional<CreditCards> reloaded = creditCardRepository.findByCardNumber(cardNumber);
        assertThat(reloaded).isPresent();
    }

    @Test
    @DisplayName("2. should return credit cards when find by id number")
    void shouldReturnCreditCardsWhenFindByIdNumber() {
        List<CreditCards> reloaded = creditCardRepository.findAllByIdNumber(idNumber);
        assertThat(reloaded).isNotEmpty();
    }

    @Test
    @DisplayName("3. should not return credit card when find by invalid card number")
    void shouldNotReturnCreditCardWhenFindByInvalidCardNumber() {
        Optional<CreditCards> reloaded = creditCardRepository.findByCardNumber(CreditCardsUtilities.updatedCardNumber());
        assertThat(reloaded).isNotPresent();
    }

    @Test
    @DisplayName("4. should not return credit cards when find by invalid id number")
    void shouldNotReturnCreditCardsWhenFindByInvalidIdNumber() {
        List<CreditCards> reloaded = creditCardRepository.findAllByIdNumber(CreditCardsUtilities.updatedIdNumber());
        assertThat(reloaded).isEmpty();
    }

    @Test
    @DisplayName("5. should return credit card transactions by id number and date range")
    void shouldReturnCreditCardTransactionsByIdNumberAndDateRange() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate today = LocalDate.now();

        Page<CreditCardTransactions> reloaded = creditCardTransactionRepository.findByCreditCardIdNumberAndDateBetween(
                idNumber,
                pageable,
                today.minusDays(1),
                today.plusDays(1));

        assertThat(reloaded.getContent()).isNotEmpty();
        assertThat(reloaded.getContent())
                .extracting(tx -> tx.getCreditCard().getIdNumber())
                .containsOnly(idNumber);
    }
}

