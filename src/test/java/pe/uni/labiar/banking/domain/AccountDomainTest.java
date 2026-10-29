package pe.uni.labiar.banking.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.uni.labiar.banking.domain.exception.InactiveAccountException;
import pe.uni.labiar.banking.domain.exception.InsufficientFundsException;
import pe.uni.labiar.banking.domain.exception.InvalidAmountException;
import pe.uni.labiar.banking.domain.model.Account;
import pe.uni.labiar.banking.domain.model.AccountStatus;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias del Dominio de Cuentas")
class AccountDomainTest {

    @Test
    @DisplayName("Debe crear cuenta correctamente con saldo inicial")
    void shouldCreateAccountSuccessfully() {
        Account account = new Account(1L, "PE-12345678", "Gustavo Lopez", "70809010", new BigDecimal("500.00"), "PEN");
        assertEquals("PE-12345678", account.getAccountNumber());
        assertEquals(new BigDecimal("500.00"), account.getBalance());
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
        assertEquals("PEN", account.getCurrency());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el saldo inicial es negativo")
    void shouldThrowExceptionWhenInitialBalanceIsNegative() {
        assertThrows(InvalidAmountException.class, () ->
            new Account(1L, "PE-12345678", "Gustavo Lopez", "70809010", new BigDecimal("-10.00"), "PEN")
        );
    }

    @Test
    @DisplayName("Debe realizar depósito e incrementar saldo correctamente")
    void shouldDepositSuccessfully() {
        Account account = new Account(1L, "PE-12345678", "Gustavo Lopez", "70809010", new BigDecimal("100.00"), "PEN");
        account.deposit(new BigDecimal("250.50"));
        assertEquals(new BigDecimal("350.50"), account.getBalance());
    }

    @Test
    @DisplayName("Debe realizar retiro si dispone de saldo suficiente")
    void shouldWithdrawSuccessfullyWhenFundsAreSufficient() {
        Account account = new Account(1L, "PE-12345678", "Gustavo Lopez", "70809010", new BigDecimal("500.00"), "PEN");
        account.withdraw(new BigDecimal("200.00"));
        assertEquals(new BigDecimal("300.00"), account.getBalance());
    }

    @Test
    @DisplayName("Debe lanzar InsufficientFundsException cuando el retiro supera el saldo disponible")
    void shouldThrowExceptionWhenFundsAreInsufficient() {
        Account account = new Account(1L, "PE-12345678", "Gustavo Lopez", "70809010", new BigDecimal("150.00"), "PEN");
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(new BigDecimal("200.00")));
    }

    @Test
    @DisplayName("Debe rechazar operaciones si la cuenta está bloqueada")
    void shouldRejectOperationsWhenAccountIsBlocked() {
        Account account = new Account(1L, "PE-12345678", "Gustavo Lopez", "70809010", new BigDecimal("1000.00"), "PEN");
        account.setStatus(AccountStatus.BLOCKED);
        assertThrows(InactiveAccountException.class, () -> account.deposit(new BigDecimal("100.00")));
        assertThrows(InactiveAccountException.class, () -> account.withdraw(new BigDecimal("50.00")));
    }
}
