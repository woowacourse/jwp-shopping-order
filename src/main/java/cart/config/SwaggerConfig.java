package cart.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info().title("장바구니 주문 api").version("0.0.1")
                .description("장바구니의 상품을 주문하고 포인트를 적립하는 api입니다.");

        SecurityScheme basichAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("basic");
        SecurityRequirement securityItem = new SecurityRequirement().addList("basicAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("basicAuth", basichAuth))
                .addSecurityItem(securityItem)
                .info(info);
    }
}
