package za.co.capitec.creditcards.creditcardsTests.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import za.co.capitec.creditcards.dtos.records.CreditCardRecord;
import za.co.capitec.creditcards.dtos.requests.CreateCreditCardDto;
import za.co.capitec.creditcards.dtos.requests.UpdateCreditCardDto;
import za.co.capitec.creditcards.entity.CreditCards;
import za.co.capitec.creditcards.enums.CreditCardType;
import za.co.capitec.creditcards.exceptions.ResourceAlreadyExistsException;
import za.co.capitec.creditcards.exceptions.ResourceNotFoundException;
import za.co.capitec.creditcards.repositories.CreditCardRepository;
import za.co.capitec.creditcards.services.impl.CreditCardServiceImpl;
import za.co.capitec.creditcards.utils.CreditCardsUtilities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class CreditCardServiceImplTest {

    private CreditCardServiceImpl underTestService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CreditCardRepository creditCardRepository;

    @Captor
    private ArgumentCaptor<CreditCards> creditCardCaptor;

    private CreditCards activeCard;
    private CreditCards inactiveCard;
    private CreateCreditCardDto createDto;

    @BeforeEach
    void setUp() {
        underTestService = new CreditCardServiceImpl(creditCardRepository, modelMapper);

        activeCard = CreditCards.builder()
                .id(1L)
                .cardNumber(CreditCardsUtilities.setCardNumber())
                .accountNumber(1234567890L)
                .cardType(CreditCardType.VISA)
                .mobileNumber(CreditCardsUtilities.setContactNumber())
                .idNumber(CreditCardsUtilities.setIdNumber())
                .amount(new BigDecimal("1000.00"))
                .creditLimit(new BigDecimal("5000.00"))
                .availableCredit(new BigDecimal("4000.00"))
                .outstandingBalance(new BigDecimal("1000.00"))
                .minimumPayment(new BigDecimal("100.00"))
                .issueDate(LocalDate.now().minusMonths(3))
                .expiryDate(LocalDate.now().plusYears(3))
                .activeSw(true)
                .build();

        inactiveCard = CreditCards.builder()
                .id(2L)
                .cardNumber(CreditCardsUtilities.updatedCardNumber())
                .accountNumber(1234567891L)
                .cardType(CreditCardType.AMEX)
                .mobileNumber(CreditCardsUtilities.updatedContactNumber())
                .idNumber(CreditCardsUtilities.setIdNumber())
                .amount(new BigDecimal("0.00"))
                .creditLimit(new BigDecimal("2500.00"))
                .availableCredit(new BigDecimal("0.00"))
                .outstandingBalance(new BigDecimal("0.00"))
                .minimumPayment(new BigDecimal("0.00"))
                .issueDate(LocalDate.now().minusMonths(5))
                .expiryDate(LocalDate.now().plusYears(1))
                .activeSw(false)
                .build();

        createDto = new CreateCreditCardDto(
                CreditCardType.VISA,
                CreditCardsUtilities.setContactNumber(),
                CreditCardsUtilities.setIdNumber(),
                3000.00,
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusYears(2),
                true);

        lenient().when(modelMapper.map(createDto, CreditCards.class)).thenReturn(
                CreditCards.builder()
                        .cardType(createDto.getCardType())
                        .mobileNumber(createDto.getMobileNumber())
                        .idNumber(createDto.getIdNumber())
                        .creditLimit(BigDecimal.valueOf(createDto.getCreditLimit()))
                        .issueDate(createDto.getIssueDate())
                        .expiryDate(createDto.getExpiryDate())
                        .activeSw(createDto.isActiveSw())
                        .build());

        lenient().when(modelMapper.map(activeCard, CreditCardRecord.class)).thenReturn(
                new CreditCardRecord(
                        activeCard.getCardNumber(),
                        activeCard.getCardType(),
                        activeCard.getMobileNumber(),
                        activeCard.getIdNumber(),
                        activeCard.getCreditLimit().doubleValue(),
                        activeCard.getAvailableCredit().doubleValue(),
                        activeCard.getOutstandingBalance().doubleValue(),
                        activeCard.getMinimumPayment().doubleValue(),
                        activeCard.getIssueDate(),
                        activeCard.getExpiryDate(),
                        activeCard.isActiveSw()));

        lenient().when(modelMapper.map(inactiveCard, CreditCardRecord.class)).thenReturn(
                new CreditCardRecord(
                        inactiveCard.getCardNumber(),
                        inactiveCard.getCardType(),
                        inactiveCard.getMobileNumber(),
                        inactiveCard.getIdNumber(),
                        inactiveCard.getCreditLimit().doubleValue(),
                        inactiveCard.getAvailableCredit().doubleValue(),
                        inactiveCard.getOutstandingBalance().doubleValue(),
                        inactiveCard.getMinimumPayment().doubleValue(),
                        inactiveCard.getIssueDate(),
                        inactiveCard.getExpiryDate(),
                        inactiveCard.isActiveSw()));
    }

    @Test
    @DisplayName("1. should return active credit cards by id number")
    void shouldReturnActiveCreditCardsByIdNumber() {
        when(creditCardRepository.existsByIdNumber(CreditCardsUtilities.setIdNumber())).thenReturn(true);
        when(creditCardRepository.findAllByIdNumber(CreditCardsUtilities.setIdNumber())).thenReturn(List.of(activeCard, inactiveCard));

        List<CreditCardRecord> result = underTestService.findCreditCardsByIdNumber(CreditCardsUtilities.setIdNumber());

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getCardNumber()).isEqualTo(activeCard.getCardNumber());
        verify(creditCardRepository).existsByIdNumber(CreditCardsUtilities.setIdNumber());
        verify(creditCardRepository).findAllByIdNumber(CreditCardsUtilities.setIdNumber());
    }

    @Test
    @DisplayName("2. should throw when id number does not exist")
    void shouldThrowWhenIdNumberDoesNotExist() {
        when(creditCardRepository.existsByIdNumber(CreditCardsUtilities.setIdNumber())).thenReturn(false);

        assertThatThrownBy(() -> underTestService.findCreditCardsByIdNumber(CreditCardsUtilities.setIdNumber()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ID Number");

        verify(creditCardRepository).existsByIdNumber(CreditCardsUtilities.setIdNumber());
        verify(creditCardRepository, never()).findAllByIdNumber(CreditCardsUtilities.setIdNumber());
    }

    @Test
    @DisplayName("3. should create credit card when unique fields do not exist")
    void shouldCreateCreditCardWhenUniqueFieldsDoNotExist() {
        underTestService.createCreditCard(createDto);

        verify(creditCardRepository).save(creditCardCaptor.capture());
        CreditCards saved = creditCardCaptor.getValue();
        assertThat(saved.getMobileNumber()).isEqualTo(createDto.getMobileNumber());
        assertThat(saved.getIdNumber()).isEqualTo(createDto.getIdNumber());
        assertThat(saved.getCardNumber()).isNotNull();
        assertThat(saved.getAccountNumber()).isNotNull();
    }

    @Test
    @DisplayName("4. should throw when creating duplicate by id number")
    void shouldThrowWhenCreatingDuplicateByIdNumber() {
        when(creditCardRepository.existsByIdNumber(createDto.getIdNumber())).thenReturn(true);

        assertThatThrownBy(() -> underTestService.createCreditCard(createDto))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("ID Number");

        verify(creditCardRepository, never()).save(any());
    }

    @Test
    @DisplayName("5. should update credit card by card number")
    void shouldUpdateCreditCardByCardNumber() {
        UpdateCreditCardDto updateDto = new UpdateCreditCardDto(
                CreditCardsUtilities.updatedContactNumber(),
                CreditCardsUtilities.updatedIdNumber(),
                new BigDecimal("7000.00"),
                LocalDate.now().plusYears(4),
                true);

        when(creditCardRepository.findByCardNumber(activeCard.getCardNumber())).thenReturn(Optional.of(activeCard));

        underTestService.updateCreditCardByCardNumber(activeCard.getCardNumber(), updateDto);

        verify(creditCardRepository).save(creditCardCaptor.capture());
        CreditCards updated = creditCardCaptor.getValue();
        assertThat(updated.getMobileNumber()).isEqualTo(updateDto.getMobileNumber());
        assertThat(updated.getIdNumber()).isEqualTo(updateDto.getIdNumber());
        assertThat(updated.getCreditLimit()).isEqualByComparingTo(updateDto.getCreditLimit());
    }

    @Test
    @DisplayName("6. should soft delete credit card by card number")
    void shouldSoftDeleteCreditCardByCardNumber() {
        when(creditCardRepository.findByCardNumber(activeCard.getCardNumber())).thenReturn(Optional.of(activeCard));

        underTestService.deleteCreditCardByCardNumber(activeCard.getCardNumber());

        verify(creditCardRepository).save(creditCardCaptor.capture());
        CreditCards deleted = creditCardCaptor.getValue();
        assertThat(deleted.getAvailableCredit()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(deleted.isActiveSw()).isFalse();
    }

    @Test
    @DisplayName("7. should throw when card number is not found")
    void shouldThrowWhenCardNumberIsNotFound() {
        when(creditCardRepository.findByCardNumber(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTestService.findByCardNumber(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Card Number");

        verify(creditCardRepository).findByCardNumber(999L);
        verifyNoMoreInteractions(creditCardRepository);
    }
}

