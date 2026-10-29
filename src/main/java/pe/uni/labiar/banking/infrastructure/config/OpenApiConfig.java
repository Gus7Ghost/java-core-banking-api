package pe.uni.labiar.banking.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI coreBankingOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Core Banking & Transactional Microservice API")
                .description("Microservicio transaccional financiero desarrollado con Arquitectura Hexagonal y Spring Boot 3")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Gustavo Bryan López Cosme")
                    .email("glopezc@uni.pe")
                    .url("https://github.com/Gus7Ghost/java-core-banking-api"))
                .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
    }
}
