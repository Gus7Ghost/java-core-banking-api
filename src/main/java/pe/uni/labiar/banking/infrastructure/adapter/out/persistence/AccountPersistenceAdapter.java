package pe.uni.labiar.banking.infrastructure.adapter.out.persistence;

import org.springframework.stereotype.Component;
import pe.uni.labiar.banking.domain.model.Account;
import pe.uni.labiar.banking.domain.port.out.AccountRepositoryPort;

import java.util.List;
import java.util.Optional;

@Component
public class AccountPersistenceAdapter implements AccountRepositoryPort {

    private final SpringDataAccountRepository repository;

    public AccountPersistenceAdapter(SpringDataAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Account save(Account account) {
        AccountJpaEntity entity = AccountJpaEntity.fromDomain(account);
        AccountJpaEntity saved = repository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber).map(AccountJpaEntity::toDomain);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return repository.findById(id).map(AccountJpaEntity::toDomain);
    }

    @Override
    public List<Account> findAll() {
        return repository.findAll().stream().map(AccountJpaEntity::toDomain).toList();
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return repository.existsByAccountNumber(accountNumber);
    }
}
