package pe.uni.labiar.banking.application.dto;

import pe.uni.labiar.banking.domain.model.Account;
import pe.uni.labiar.banking.domain.model.AccountStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(
    Long id,
    String accountNumber,
    String ownerName,
    String ownerDni,
    BigDecimal balance,
    String currency,
    AccountStatus status,
    LocalDateTime createdAt
) {
    public static AccountResponse fromDomain(Account account) {
        return new AccountResponse(
            account.getId(),
            account.getAccountNumber(),
            account.getOwnerName(),
            account.getOwnerDni(),
            account.getBalance(),
            account.getCurrency(),
            account.getStatus(),
            account.getCreatedAt()
        );
    }
}
