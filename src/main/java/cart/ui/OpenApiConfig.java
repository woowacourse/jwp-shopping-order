package cart.ui;

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
                .title("장바구니 협업 미션 API 명세")
                .description("레벨2 장바구니 협업 미션의 API 명세입니다.");

        SecurityScheme basic = new SecurityScheme()
                .type(Type.HTTP)
                .scheme("Basic");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("basic", basic))
                .info(info);
    }
}
