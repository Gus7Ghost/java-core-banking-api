package pe.uni.labiar.banking.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import pe.uni.labiar.banking.domain.model.Account;
import pe.uni.labiar.banking.domain.model.AccountStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class AccountJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false, length = 30)
    private String accountNumber;

    @Column(name = "owner_name", nullable = false, length = 150)
    private String ownerName;

    @Column(name = "owner_dni", nullable = false, length = 15)
    private String ownerDni;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, length = 5)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AccountJpaEntity() {}

    public static AccountJpaEntity fromDomain(Account a) {
        AccountJpaEntity entity = new AccountJpaEntity();
        entity.setId(a.getId());
        entity.setAccountNumber(a.getAccountNumber());
        entity.setOwnerName(a.getOwnerName());
        entity.setOwnerDni(a.getOwnerDni());
        entity.setBalance(a.getBalance());
        entity.setCurrency(a.getCurrency());
        entity.setStatus(a.getStatus());
        entity.setCreatedAt(a.getCreatedAt() != null ? a.getCreatedAt() : LocalDateTime.now());
        return entity;
    }

    public Account toDomain() {
        Account a = new Account();
        a.setId(this.id);
        a.setAccountNumber(this.accountNumber);
        a.setOwnerName(this.ownerName);
        a.setOwnerDni(this.ownerDni);
        a.setBalance(this.balance);
        a.setCurrency(this.currency);
        a.setStatus(this.status);
        a.setCreatedAt(this.createdAt);
        return a;
    }

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
