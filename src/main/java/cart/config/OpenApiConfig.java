package cart.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0.0")
                .title("장바구니 API")
                .description("장바구니 API 명세");
        SecurityScheme basic = new SecurityScheme()
                .type(Type.HTTP)
                .scheme("Basic");
        return new OpenAPI().components(new Components().addSecuritySchemes("basic", basic)).info(info);
    }
}
