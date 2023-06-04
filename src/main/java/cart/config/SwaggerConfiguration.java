package cart.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("장바구니 API Document")
                .version("v1.0.0")
                .description("장바구니 API 명세서입니다.");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
