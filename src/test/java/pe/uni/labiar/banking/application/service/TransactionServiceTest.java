package pe.uni.labiar.banking.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.uni.labiar.banking.application.dto.DepositRequest;
import pe.uni.labiar.banking.application.dto.TransactionResponse;
import pe.uni.labiar.banking.application.dto.TransferRequest;
import pe.uni.labiar.banking.application.dto.WithdrawalRequest;
import pe.uni.labiar.banking.domain.exception.AccountNotFoundException;
import pe.uni.labiar.banking.domain.exception.InsufficientFundsException;
import pe.uni.labiar.banking.domain.exception.InvalidAmountException;
import pe.uni.labiar.banking.domain.model.Account;
import pe.uni.labiar.banking.domain.model.Transaction;
import pe.uni.labiar.banking.domain.model.TransactionType;
import pe.uni.labiar.banking.domain.port.out.AccountRepositoryPort;
import pe.uni.labiar.banking.domain.port.out.TransactionRepositoryPort;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias del Servicio de Transacciones")
class TransactionServiceTest {

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private TransactionRepositoryPort transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account sourceAccount;
    private Account targetAccount;

    @BeforeEach
    void setUp() {
        sourceAccount = new Account(1L, "PE-1111", "Origen S.A.C.", "10203040", new BigDecimal("1000.00"), "PEN");
        targetAccount = new Account(2L, "PE-2222", "Destino S.A.C.", "50607080", new BigDecimal("300.00"), "PEN");
    }

    @Test
    @DisplayName("Debe procesar transferencia exitosa entre dos cuentas en misma moneda")
    void shouldProcessTransferSuccessfully() {
        when(accountRepository.findByAccountNumber("PE-1111")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("PE-2222")).thenReturn(Optional.of(targetAccount));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransferRequest request = new TransferRequest("PE-1111", "PE-2222", new BigDecimal("400.00"), "Pago de factura");
        TransactionResponse response = transactionService.transfer(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("400.00"), response.amount());
        assertEquals(new BigDecimal("600.00"), sourceAccount.getBalance());
        assertEquals(new BigDecimal("700.00"), targetAccount.getBalance());
        verify(accountRepository, times(1)).save(sourceAccount);
        verify(accountRepository, times(1)).save(targetAccount);
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Debe rechazar transferencia si la cuenta origen no tiene fondos suficientes")
    void shouldRejectTransferWhenInsufficientFunds() {
        when(accountRepository.findByAccountNumber("PE-1111")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("PE-2222")).thenReturn(Optional.of(targetAccount));

        TransferRequest request = new TransferRequest("PE-1111", "PE-2222", new BigDecimal("2500.00"), "Monto excesivo");

        assertThrows(InsufficientFundsException.class, () -> transactionService.transfer(request));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe rechazar transferencia entre monedas incompatibles (PEN vs USD)")
    void shouldRejectTransferWhenCurrenciesDoNotMatch() {
        targetAccount.setCurrency("USD");
        when(accountRepository.findByAccountNumber("PE-1111")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("PE-2222")).thenReturn(Optional.of(targetAccount));

        TransferRequest request = new TransferRequest("PE-1111", "PE-2222", new BigDecimal("100.00"), "Transferencia bimonetaria");

        assertThrows(InvalidAmountException.class, () -> transactionService.transfer(request));
    }
}
