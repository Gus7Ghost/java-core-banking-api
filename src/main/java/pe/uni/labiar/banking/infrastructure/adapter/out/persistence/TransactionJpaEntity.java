package pe.uni.labiar.banking.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import pe.uni.labiar.banking.domain.model.Transaction;
import pe.uni.labiar.banking.domain.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class TransactionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference", nullable = false, length = 50)
    private String transactionReference;

    @Column(name = "source_account", nullable = false, length = 30)
    private String sourceAccountNumber;

    @Column(name = "target_account", length = 30)
    private String targetAccountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "resulting_balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal resultingBalance;

    @Column(nullable = false, length = 5)
    private String currency;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 255)
    private String description;

    public TransactionJpaEntity() {}

    public static TransactionJpaEntity fromDomain(Transaction t) {
        TransactionJpaEntity entity = new TransactionJpaEntity();
        entity.setId(t.getId());
        entity.setTransactionReference(t.getTransactionReference());
        entity.setSourceAccountNumber(t.getSourceAccountNumber());
        entity.setTargetAccountNumber(t.getTargetAccountNumber());
        entity.setType(t.getType());
        entity.setAmount(t.getAmount());
        entity.setResultingBalance(t.getResultingBalance());
        entity.setCurrency(t.getCurrency());
        entity.setTimestamp(t.getTimestamp() != null ? t.getTimestamp() : LocalDateTime.now());
        entity.setDescription(t.getDescription());
        return entity;
    }

    public Transaction toDomain() {
        Transaction t = new Transaction();
        t.setId(this.id);
        t.setTransactionReference(this.transactionReference);
        t.setSourceAccountNumber(this.sourceAccountNumber);
        t.setTargetAccountNumber(this.targetAccountNumber);
        t.setType(this.type);
        t.setAmount(this.amount);
        t.setResultingBalance(this.resultingBalance);
        t.setCurrency(this.currency);
        t.setTimestamp(this.timestamp);
        t.setDescription(this.description);
        return t;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public void setSourceAccountNumber(String sourceAccountNumber) { this.sourceAccountNumber = sourceAccountNumber; }
    public String getTargetAccountNumber() { return targetAccountNumber; }
    public void setTargetAccountNumber(String targetAccountNumber) { this.targetAccountNumber = targetAccountNumber; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getResultingBalance() { return resultingBalance; }
    public void setResultingBalance(BigDecimal resultingBalance) { this.resultingBalance = resultingBalance; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
