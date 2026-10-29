package pe.uni.labiar.banking.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private Long id;
    private String transactionReference;
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private TransactionType type;
    private BigDecimal amount;
    private BigDecimal resultingBalance;
    private String currency;
    private LocalDateTime timestamp;
    private String description;

    public Transaction() {}

    public Transaction(Long id, String transactionReference, String sourceAccountNumber, String targetAccountNumber,
                       TransactionType type, BigDecimal amount, BigDecimal resultingBalance, String currency, String description) {
        this.id = id;
        this.transactionReference = transactionReference;
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
        this.type = type;
        this.amount = amount;
        this.resultingBalance = resultingBalance;
        this.currency = currency;
        this.description = description;
        this.timestamp = LocalDateTime.now();
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
