package pe.uni.labiar.banking.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransferRequest(
    @NotBlank(message = "El número de cuenta de origen es obligatorio")
    String sourceAccountNumber,

    @NotBlank(message = "El número de cuenta de destino es obligatorio")
    String targetAccountNumber,

    @NotNull(message = "El monto a transferir es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto a transferir debe ser mayor a 0.00")
    BigDecimal amount,

    String description
) {}
