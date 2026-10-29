package pe.uni.labiar.banking.infrastructure.adapter.out.persistence;

import org.springframework.stereotype.Component;
import pe.uni.labiar.banking.domain.model.Transaction;
import pe.uni.labiar.banking.domain.port.out.TransactionRepositoryPort;

import java.util.List;

@Component
public class TransactionPersistenceAdapter implements TransactionRepositoryPort {

    private final SpringDataTransactionRepository repository;

    public TransactionPersistenceAdapter(SpringDataTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionJpaEntity entity = TransactionJpaEntity.fromDomain(transaction);
        TransactionJpaEntity saved = repository.save(entity);
        return saved.toDomain();
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber) {
        return repository.findByAccount(accountNumber).stream()
            .map(TransactionJpaEntity::toDomain)
            .toList();
    }

    @Override
    public List<Transaction> findAll() {
        return repository.findAll().stream()
            .map(TransactionJpaEntity::toDomain)
            .toList();
    }
}
