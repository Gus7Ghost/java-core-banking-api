package pe.uni.labiar.banking.infrastructure.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.uni.labiar.banking.domain.exception.AccountNotFoundException;
import pe.uni.labiar.banking.domain.exception.InactiveAccountException;
import pe.uni.labiar.banking.domain.exception.InsufficientFundsException;
import pe.uni.labiar.banking.domain.exception.InvalidAmountException;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFound(AccountNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Cuenta No Encontrada");
        problem.setType(URI.create("https://errors.labiar.uni.pe/account-not-found"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ProblemDetail handleInsufficientFunds(InsufficientFundsException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problem.setTitle("Fondos Insuficientes");
        problem.setType(URI.create("https://errors.labiar.uni.pe/insufficient-funds"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(InactiveAccountException.class)
    public ProblemDetail handleInactiveAccount(InactiveAccountException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Cuenta Inactiva o Bloqueada");
        problem.setType(URI.create("https://errors.labiar.uni.pe/inactive-account"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ProblemDetail handleInvalidAmount(InvalidAmountException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Parámetros de Transacción Inválidos");
        problem.setType(URI.create("https://errors.labiar.uni.pe/invalid-amount"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Error de validación en los campos enviados");
        problem.setTitle("Validación Fallida");
        problem.setType(URI.create("https://errors.labiar.uni.pe/validation-error"));
        
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        problem.setProperty("invalidFields", fieldErrors);
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
