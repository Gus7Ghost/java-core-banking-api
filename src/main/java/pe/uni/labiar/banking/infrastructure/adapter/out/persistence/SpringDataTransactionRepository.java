package pe.uni.labiar.banking.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataTransactionRepository extends JpaRepository<TransactionJpaEntity, Long> {
    
    @Query("SELECT t FROM TransactionJpaEntity t WHERE t.sourceAccountNumber = :acc OR t.targetAccountNumber = :acc ORDER BY t.timestamp DESC")
    List<TransactionJpaEntity> findByAccount(@Param("acc") String accountNumber);
}
