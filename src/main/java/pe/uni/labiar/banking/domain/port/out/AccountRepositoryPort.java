package pe.uni.labiar.banking.domain.port.out;

import pe.uni.labiar.banking.domain.model.Account;
import java.util.Optional;
import java.util.List;

public interface AccountRepositoryPort {
    Account save(Account account);
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findById(Long id);
    List<Account> findAll();
    boolean existsByAccountNumber(String accountNumber);
}
