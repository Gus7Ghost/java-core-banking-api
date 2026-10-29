package pe.uni.labiar.banking.infrastructure.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.uni.labiar.banking.application.dto.DepositRequest;
import pe.uni.labiar.banking.application.dto.TransactionResponse;
import pe.uni.labiar.banking.application.dto.TransferRequest;
import pe.uni.labiar.banking.application.dto.WithdrawalRequest;
import pe.uni.labiar.banking.application.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transactions", description = "Endpoints de operaciones monetarias: Depósitos, Retiros y Transferencias")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    @Operation(summary = "Realizar depósito", description = "Abona fondos a una cuenta bancaria activa")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.deposit(request));
    }

    @PostMapping("/withdraw")
    @Operation(summary = "Realizar retiro", description = "Debita fondos de una cuenta si dispone de saldo suficiente")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.withdraw(request));
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transferencia entre cuentas", description = "Transfiere fondos atómicamente entre dos cuentas de la misma moneda")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.transfer(request));
    }

    @GetMapping("/history/{accountNumber}")
    @Operation(summary = "Historial de transacciones", description = "Lista cronológicamente todas las operaciones de una cuenta")
    public ResponseEntity<List<TransactionResponse>> getHistory(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getAccountHistory(accountNumber));
    }
}
