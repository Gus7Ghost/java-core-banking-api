package pe.uni.labiar.banking.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record WithdrawalRequest(
    @NotBlank(message = "El número de cuenta es obligatorio")
    String accountNumber,

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto a retirar debe ser mayor a 0.00")
    BigDecimal amount,

    String description
) {}
