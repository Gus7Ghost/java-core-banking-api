package pe.uni.labiar.banking.domain.port.out;

import pe.uni.labiar.banking.domain.model.Transaction;
import java.util.List;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);
    List<Transaction> findByAccountNumber(String accountNumber);
    List<Transaction> findAll();
}
