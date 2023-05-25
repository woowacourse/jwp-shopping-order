package cart.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPI() {

        final Info info = new Info()
                .version("v1.0.0")
                .title("장바구니 API")
                .description("장바구니 API 명세");

        final SecurityScheme basicAuth = new SecurityScheme().type(Type.HTTP).scheme("basic");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("basicAuth", basicAuth))
                .info(info);
    }
}
