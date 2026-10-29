package pe.uni.labiar.banking.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public record CreateAccountRequest(
    @NotBlank(message = "El nombre del titular es obligatorio")
    String ownerName,

    @NotBlank(message = "El DNI del titular es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos numéricos")
    String ownerDni,

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.00", message = "El saldo inicial no puede ser negativo")
    BigDecimal initialBalance,

    @NotBlank(message = "La moneda es obligatoria (PEN o USD)")
    @Pattern(regexp = "PEN|USD", message = "Moneda permitida: PEN o USD")
    String currency
) {}
