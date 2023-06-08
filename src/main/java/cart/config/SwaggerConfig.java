package cart.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "장바구니 API 명세서", version = "v1"))
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi cartOrderApi() {
        String[] paths = {"/orders/**", "/products/**", "/cart-items/**"};

        return GroupedOpenApi.builder()
                .group("장바구니 API")
                .pathsToMatch(paths)
                .build();
    }
}
