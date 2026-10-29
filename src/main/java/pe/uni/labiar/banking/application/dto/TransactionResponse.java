package pe.uni.labiar.banking.application.dto;

import pe.uni.labiar.banking.domain.model.Transaction;
import pe.uni.labiar.banking.domain.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
    Long id,
    String reference,
    String sourceAccount,
    String targetAccount,
    TransactionType type,
    BigDecimal amount,
    BigDecimal resultingBalance,
    String currency,
    LocalDateTime timestamp,
    String description
) {
    public static TransactionResponse fromDomain(Transaction t) {
        return new TransactionResponse(
            t.getId(),
            t.getTransactionReference(),
            t.getSourceAccountNumber(),
            t.getTargetAccountNumber(),
            t.getType(),
            t.getAmount(),
            t.getResultingBalance(),
            t.getCurrency(),
            t.getTimestamp(),
            t.getDescription()
        );
    }
}
