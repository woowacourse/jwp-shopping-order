package cart.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(servers = {
        @Server(url = "http://localhost:8080", description = "로컬"),
        @Server(url = "https://h3rb.shop", description = "허브"),
        @Server(url = "https://m4co.shop", description = "마코"),
        @Server(url = "https://wuga.shop", description = "우가")
})
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
