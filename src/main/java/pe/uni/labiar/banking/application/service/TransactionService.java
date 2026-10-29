package pe.uni.labiar.banking.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.uni.labiar.banking.application.dto.DepositRequest;
import pe.uni.labiar.banking.application.dto.TransactionResponse;
import pe.uni.labiar.banking.application.dto.TransferRequest;
import pe.uni.labiar.banking.application.dto.WithdrawalRequest;
import pe.uni.labiar.banking.domain.exception.AccountNotFoundException;
import pe.uni.labiar.banking.domain.exception.InvalidAmountException;
import pe.uni.labiar.banking.domain.model.Account;
import pe.uni.labiar.banking.domain.model.Transaction;
import pe.uni.labiar.banking.domain.model.TransactionType;
import pe.uni.labiar.banking.domain.port.out.AccountRepositoryPort;
import pe.uni.labiar.banking.domain.port.out.TransactionRepositoryPort;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TransactionService {

    private final AccountRepositoryPort accountRepository;
    private final TransactionRepositoryPort transactionRepository;

    public TransactionService(AccountRepositoryPort accountRepository, TransactionRepositoryPort transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse deposit(DepositRequest request) {
        Account account = findAccountOrThrow(request.accountNumber());
        account.deposit(request.amount());
        accountRepository.save(account);

        Transaction transaction = new Transaction(
            null,
            generateReference("DEP"),
            account.getAccountNumber(),
            null,
            TransactionType.DEPOSIT,
            request.amount(),
            account.getBalance(),
            account.getCurrency(),
            request.description() != null ? request.description() : "Depósito en cuenta"
        );

        Transaction saved = transactionRepository.save(transaction);
        return TransactionResponse.fromDomain(saved);
    }

    public TransactionResponse withdraw(WithdrawalRequest request) {
        Account account = findAccountOrThrow(request.accountNumber());
        account.withdraw(request.amount());
        accountRepository.save(account);

        Transaction transaction = new Transaction(
            null,
            generateReference("RET"),
            account.getAccountNumber(),
            null,
            TransactionType.WITHDRAWAL,
            request.amount(),
            account.getBalance(),
            account.getCurrency(),
            request.description() != null ? request.description() : "Retiro de efectivo"
        );

        Transaction saved = transactionRepository.save(transaction);
        return TransactionResponse.fromDomain(saved);
    }

    public TransactionResponse transfer(TransferRequest request) {
        if (request.sourceAccountNumber().equalsIgnoreCase(request.targetAccountNumber())) {
            throw new InvalidAmountException("La cuenta de origen y de destino no pueden ser la misma");
        }

        Account source = findAccountOrThrow(request.sourceAccountNumber());
        Account target = findAccountOrThrow(request.targetAccountNumber());

        if (!source.getCurrency().equalsIgnoreCase(target.getCurrency())) {
            throw new InvalidAmountException(
                String.format("Transferencia no permitida entre monedas distintas (%s vs %s)",
                    source.getCurrency(), target.getCurrency())
            );
        }

        // Ejecución en la misma unidad transaccional
        source.withdraw(request.amount());
        target.deposit(request.amount());

        accountRepository.save(source);
        accountRepository.save(target);

        String ref = generateReference("TRF");
        String desc = request.description() != null ? request.description() : "Transferencia de fondos";

        // Registrar transacción de salida en cuenta origen
        Transaction outTx = new Transaction(
            null,
            ref,
            source.getAccountNumber(),
            target.getAccountNumber(),
            TransactionType.TRANSFER_OUT,
            request.amount(),
            source.getBalance(),
            source.getCurrency(),
            desc + " -> Hacia cuenta " + target.getAccountNumber()
        );
        Transaction savedOut = transactionRepository.save(outTx);

        // Registrar transacción de entrada en cuenta destino
        Transaction inTx = new Transaction(
            null,
            ref,
            source.getAccountNumber(),
            target.getAccountNumber(),
            TransactionType.TRANSFER_IN,
            request.amount(),
            target.getBalance(),
            target.getCurrency(),
            desc + " <- Desde cuenta " + source.getAccountNumber()
        );
        transactionRepository.save(inTx);

        return TransactionResponse.fromDomain(savedOut);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getAccountHistory(String accountNumber) {
        findAccountOrThrow(accountNumber);
        return transactionRepository.findByAccountNumber(accountNumber).stream()
            .map(TransactionResponse::fromDomain)
            .toList();
    }

    private Account findAccountOrThrow(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException("Cuenta bancaria no encontrada: " + accountNumber));
    }

    private String generateReference(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
