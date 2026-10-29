package pe.uni.labiar.banking.infrastructure.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.uni.labiar.banking.application.dto.AccountResponse;
import pe.uni.labiar.banking.application.dto.CreateAccountRequest;
import pe.uni.labiar.banking.application.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Accounts", description = "Endpoints para creación y consulta de cuentas bancarias")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @Operation(summary = "Crear nueva cuenta bancaria", description = "Genera un número de cuenta único con saldo y moneda validada")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{accountNumber}")
    @Operation(summary = "Obtener cuenta por número", description = "Retorna los datos de balance y titularidad de una cuenta")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountByNumber(accountNumber));
    }

    @GetMapping
    @Operation(summary = "Listar todas las cuentas", description = "Retorna el catálogo completo de cuentas registradas")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }
}
