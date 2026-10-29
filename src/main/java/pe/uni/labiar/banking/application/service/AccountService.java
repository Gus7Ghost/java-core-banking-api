package pe.uni.labiar.banking.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.uni.labiar.banking.application.dto.AccountResponse;
import pe.uni.labiar.banking.application.dto.CreateAccountRequest;
import pe.uni.labiar.banking.domain.exception.AccountNotFoundException;
import pe.uni.labiar.banking.domain.model.Account;
import pe.uni.labiar.banking.domain.port.out.AccountRepositoryPort;

import java.security.SecureRandom;
import java.util.List;

@Service
@Transactional
public class AccountService {

    private final AccountRepositoryPort accountRepository;
    private final SecureRandom random = new SecureRandom();

    public AccountService(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponse createAccount(CreateAccountRequest request) {
        String generatedAccountNumber = generateUniqueAccountNumber();
        Account newAccount = new Account(
            null,
            generatedAccountNumber,
            request.ownerName().trim(),
            request.ownerDni().trim(),
            request.initialBalance(),
            request.currency()
        );

        Account saved = accountRepository.save(newAccount);
        return AccountResponse.fromDomain(saved);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException("Cuenta bancaria no encontrada: " + accountNumber));
        return AccountResponse.fromDomain(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
            .map(AccountResponse::fromDomain)
            .toList();
    }

    private String generateUniqueAccountNumber() {
        String accNum;
        do {
            long number = 1000_0000_0000L + (Math.abs(random.nextLong()) % 9000_0000_0000L);
            accNum = "PE-" + number;
        } while (accountRepository.existsByAccountNumber(accNum));
        return accNum;
    }
}
