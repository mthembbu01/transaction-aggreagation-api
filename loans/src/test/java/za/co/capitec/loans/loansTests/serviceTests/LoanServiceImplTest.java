package za.co.capitec.loans.loansTests.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import za.co.capitec.loans.dtos.records.LoanRecord;
import za.co.capitec.loans.dtos.requests.CreateLoanDto;
import za.co.capitec.loans.dtos.requests.UpdateLoanDto;
import za.co.capitec.loans.entity.Loans;
import za.co.capitec.loans.enums.LoanStatus;
import za.co.capitec.loans.enums.LoanType;
import za.co.capitec.loans.exceptions.ResourceAlreadyExistsException;
import za.co.capitec.loans.exceptions.ResourceNotFoundException;
import za.co.capitec.loans.repositories.LoanRepository;
import za.co.capitec.loans.services.impl.LoanServiceImpl;
import za.co.capitec.loans.utils.LoansUtilities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    private LoanServiceImpl underTestService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private LoanRepository loanRepository;

    @Captor
    private ArgumentCaptor<Loans> loanArgumentCaptor;

    private Loans activeLoan;
    private Loans inactiveLoan;
    private CreateLoanDto createLoanDto;

    @BeforeEach
    void setUp() {
        underTestService = new LoanServiceImpl(loanRepository, modelMapper);

        activeLoan = Loans.builder()
                .id(1L)
                .loanNumber(LoansUtilities.setLoanNumber())
                .loanType(LoanType.PERSONAL)
                .mobileNumber(LoansUtilities.setContactNumber())
                .idNumber(LoansUtilities.setIdNumber())
                .loanAmount(new BigDecimal("5000.00"))
                .outstandingBalance(new BigDecimal("5000.00"))
                .monthlyInstalment(new BigDecimal("500.00"))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(12))
                .status(LoanStatus.ACTIVE)
                .activeSw(true)
                .build();

        inactiveLoan = Loans.builder()
                .id(2L)
                .loanNumber(LoansUtilities.updatedLoanNumber())
                .loanType(LoanType.HOME)
                .mobileNumber(LoansUtilities.updatedContactNumber())
                .idNumber(LoansUtilities.setIdNumber())
                .loanAmount(new BigDecimal("9000.00"))
                .outstandingBalance(new BigDecimal("0.00"))
                .monthlyInstalment(new BigDecimal("900.00"))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(10))
                .status(LoanStatus.CLOSED)
                .activeSw(false)
                .build();

        createLoanDto = new CreateLoanDto(
                LoanType.PERSONAL,
                LoansUtilities.setContactNumber(),
                LoansUtilities.setIdNumber(),
                new BigDecimal("3000.00"),
                new BigDecimal("250.00"),
                LocalDate.now(),
                LocalDate.now().plusMonths(12),
                true);

        lenient().when(modelMapper.map(createLoanDto, Loans.class)).thenReturn(
                Loans.builder()
                        .loanType(createLoanDto.getLoanType())
                        .mobileNumber(createLoanDto.getMobileNumber())
                        .idNumber(createLoanDto.getIdNumber())
                        .loanAmount(createLoanDto.getLoanAmount())
                        .monthlyInstalment(createLoanDto.getMonthlyInstalment())
                        .startDate(createLoanDto.getStartDate())
                        .endDate(createLoanDto.getEndDate())
                        .activeSw(createLoanDto.isActiveSw())
                        .build());

        lenient().when(modelMapper.map(activeLoan, LoanRecord.class)).thenReturn(
                new LoanRecord(
                        activeLoan.getLoanNumber(),
                        activeLoan.getLoanType(),
                        activeLoan.getMobileNumber(),
                        activeLoan.getIdNumber(),
                        activeLoan.getLoanAmount().doubleValue(),
                        activeLoan.getOutstandingBalance().doubleValue(),
                        activeLoan.getMonthlyInstalment().doubleValue(),
                        activeLoan.getStartDate(),
                        activeLoan.getEndDate(),
                        activeLoan.getStatus(),
                        activeLoan.isActiveSw()));

        lenient().when(modelMapper.map(inactiveLoan, LoanRecord.class)).thenReturn(
                new LoanRecord(
                        inactiveLoan.getLoanNumber(),
                        inactiveLoan.getLoanType(),
                        inactiveLoan.getMobileNumber(),
                        inactiveLoan.getIdNumber(),
                        inactiveLoan.getLoanAmount().doubleValue(),
                        inactiveLoan.getOutstandingBalance().doubleValue(),
                        inactiveLoan.getMonthlyInstalment().doubleValue(),
                        inactiveLoan.getStartDate(),
                        inactiveLoan.getEndDate(),
                        inactiveLoan.getStatus(),
                        inactiveLoan.isActiveSw()));
    }

    @Test
    @DisplayName("1. should return active loans by id number")
    void shouldReturnActiveLoansByIdNumber() {
        when(loanRepository.existsByIdNumber(LoansUtilities.setIdNumber())).thenReturn(true);
        when(loanRepository.findAllByIdNumber(LoansUtilities.setIdNumber())).thenReturn(List.of(activeLoan, inactiveLoan));

        List<LoanRecord> records = underTestService.findLoansByIdNumber(LoansUtilities.setIdNumber());

        assertThat(records).hasSize(1);
        assertThat(records.getFirst().getLoanNumber()).isEqualTo(activeLoan.getLoanNumber());
        verify(loanRepository).existsByIdNumber(LoansUtilities.setIdNumber());
        verify(loanRepository).findAllByIdNumber(LoansUtilities.setIdNumber());
    }

    @Test
    @DisplayName("2. should throw when id number does not exist")
    void shouldThrowWhenIdNumberDoesNotExist() {
        when(loanRepository.existsByIdNumber(LoansUtilities.setIdNumber())).thenReturn(false);

        assertThatThrownBy(() -> underTestService.findLoansByIdNumber(LoansUtilities.setIdNumber()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ID Number");

        verify(loanRepository).existsByIdNumber(LoansUtilities.setIdNumber());
        verify(loanRepository, never()).findAllByIdNumber(LoansUtilities.setIdNumber());
    }

    @Test
    @DisplayName("3. should create loan when unique fields do not exist")
    void shouldCreateLoanWhenUniqueFieldsDoNotExist() {
        underTestService.createLoan(createLoanDto);

        verify(loanRepository).save(loanArgumentCaptor.capture());
        Loans saved = loanArgumentCaptor.getValue();
        assertThat(saved.getIdNumber()).isEqualTo(createLoanDto.getIdNumber());
        assertThat(saved.getMobileNumber()).isEqualTo(createLoanDto.getMobileNumber());
        assertThat(saved.getStatus()).isEqualTo(LoanStatus.ACTIVE);
        assertThat(saved.isActiveSw()).isTrue();
        assertThat(saved.getOutstandingBalance()).isEqualByComparingTo(createLoanDto.getLoanAmount());
    }

    @Test
    @DisplayName("4. should throw when creating duplicate by id number")
    void shouldThrowWhenCreatingDuplicateByIdNumber() {
        when(loanRepository.existsByIdNumber(createLoanDto.getIdNumber())).thenReturn(true);

        assertThatThrownBy(() -> underTestService.createLoan(createLoanDto))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("ID Number");

        verify(loanRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("5. should update loan by loan number")
    void shouldUpdateLoanByLoanNumber() {
        UpdateLoanDto updateLoanDto = new UpdateLoanDto(
                LoansUtilities.updatedContactNumber(),
                LoansUtilities.updatedIdNumber(),
                new BigDecimal("4500.00"),
                new BigDecimal("375.00"),
                LocalDate.now().plusMonths(8),
                LoanStatus.PENDING,
                true);

        when(loanRepository.findByLoanNumber(activeLoan.getLoanNumber())).thenReturn(Optional.of(activeLoan));

        underTestService.updateLoanByLoanNumber(activeLoan.getLoanNumber(), updateLoanDto);

        verify(loanRepository, times(1)).save(loanArgumentCaptor.capture());
        Loans updated = loanArgumentCaptor.getValue();
        assertThat(updated.getIdNumber()).isEqualTo(updateLoanDto.getIdNumber());
        assertThat(updated.getMobileNumber()).isEqualTo(updateLoanDto.getMobileNumber());
        assertThat(updated.getLoanAmount()).isEqualByComparingTo(updateLoanDto.getLoanAmount());
        assertThat(updated.getStatus()).isEqualTo(LoanStatus.PENDING);
    }

    @Test
    @DisplayName("6. should soft delete loan by loan number")
    void shouldSoftDeleteLoanByLoanNumber() {
        when(loanRepository.findByLoanNumber(activeLoan.getLoanNumber())).thenReturn(Optional.of(activeLoan));

        underTestService.deleteLoanByLoanNumber(activeLoan.getLoanNumber());

        verify(loanRepository).save(loanArgumentCaptor.capture());
        Loans deleted = loanArgumentCaptor.getValue();
        assertThat(deleted.getOutstandingBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(deleted.getStatus()).isEqualTo(LoanStatus.CLOSED);
        assertThat(deleted.isActiveSw()).isFalse();
    }

    @Test
    @DisplayName("7. should throw when loan number is not found")
    void shouldThrowWhenLoanNumberIsNotFound() {
        when(loanRepository.findByLoanNumber(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTestService.findByLoanNumber(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Loan Number");

        verify(loanRepository).findByLoanNumber(999L);
        verifyNoMoreInteractions(loanRepository);
    }
}

