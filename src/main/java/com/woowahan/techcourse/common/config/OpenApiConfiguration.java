package com.woowahan.techcourse.common.config;

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
        @Server(url = "https://nunu.bview.store/", description = "누누"),
        @Server(url = "https://philip.bview.store", description = "필립"),
        @Server(url = "https://shopping-cart.woojin.life/", description = "체인저")
})
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0.0")
                .title("장바구니 API")
                .description("장바구니 API 명세");

        SecurityScheme basicAuth = new SecurityScheme().type(Type.HTTP).scheme("basic");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("basicAuth", basicAuth))
                .info(info);
    }
}
