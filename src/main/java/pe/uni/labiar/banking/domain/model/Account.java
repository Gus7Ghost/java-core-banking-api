package pe.uni.labiar.banking.domain.model;

import pe.uni.labiar.banking.domain.exception.InactiveAccountException;
import pe.uni.labiar.banking.domain.exception.InsufficientFundsException;
import pe.uni.labiar.banking.domain.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class Account {
    private Long id;
    private String accountNumber;
    private String ownerName;
    private String ownerDni;
    private BigDecimal balance;
    private String currency; // PEN, USD
    private AccountStatus status;
    private LocalDateTime createdAt;

    public Account() {}

    public Account(Long id, String accountNumber, String ownerName, String ownerDni, BigDecimal initialBalance, String currency) {
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("El saldo inicial no puede ser negativo");
        }
        this.id = id;
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.ownerDni = ownerDni;
        this.balance = initialBalance.setScale(2, RoundingMode.HALF_UP);
        this.currency = (currency != null) ? currency.toUpperCase() : "PEN";
        this.status = AccountStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public void deposit(BigDecimal amount) {
        validateActiveStatus();
        validatePositiveAmount(amount);
        this.balance = this.balance.add(amount).setScale(2, RoundingMode.HALF_UP);
    }

    public void withdraw(BigDecimal amount) {
        validateActiveStatus();
        validatePositiveAmount(amount);
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                String.format("Fondos insuficientes en cuenta %s. Saldo disponible: %s %s, Monto solicitado: %s %s",
                    this.accountNumber, this.currency, this.balance, this.currency, amount)
            );
        }
        this.balance = this.balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
    }

    private void validateActiveStatus() {
        if (this.status != AccountStatus.ACTIVE) {
            throw new InactiveAccountException("La cuenta " + this.accountNumber + " no se encuentra activa");
        }
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("El monto de la transacción debe ser estrictamente mayor a cero");
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getOwnerDni() { return ownerDni; }
    public void setOwnerDni(String ownerDni) { this.ownerDni = ownerDni; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
