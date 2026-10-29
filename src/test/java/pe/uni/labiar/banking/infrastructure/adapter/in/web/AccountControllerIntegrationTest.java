package pe.uni.labiar.banking.infrastructure.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pe.uni.labiar.banking.application.dto.CreateAccountRequest;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Pruebas de Integración Web API")
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/accounts - Debe retornar 201 Created al enviar payload válido")
    void shouldCreateAccountViaApi() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest(
            "Empresa Industrial UNI S.A.C.",
            "71829304",
            new BigDecimal("1500.00"),
            "PEN"
        );

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.accountNumber").exists())
            .andExpect(jsonPath("$.balance").value(1500.00))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andExpect(jsonPath("$.ownerName").value("Empresa Industrial UNI S.A.C."));
    }

    @Test
    @DisplayName("POST /api/v1/accounts - Debe retornar 400 Bad Request si el DNI es inválido")
    void shouldReturnBadRequestWhenDniIsInvalid() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest(
            "Juan Perez",
            "123", // DNI inválido
            new BigDecimal("100.00"),
            "PEN"
        );

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.invalidFields.ownerDni").exists());
    }

    @Test
    @DisplayName("GET /api/v1/accounts/{accountNumber} - Debe retornar 404 si la cuenta no existe")
    void shouldReturnNotFoundForNonExistentAccount() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/PE-INEXISTENTE"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.title").value("Cuenta No Encontrada"));
    }
}
