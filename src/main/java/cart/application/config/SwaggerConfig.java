package cart.application.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {

        final SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic");

        final SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("securityScheme");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("securityScheme", securityScheme))
                .addSecurityItem(securityRequirement);
    }
}
