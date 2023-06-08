package cart.config;

import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("상품 API")
                .consumes(consumeContentTypes())
                .produces(produceContentTypes())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cart.ui"))
                .paths(PathSelectors.regex("/products.*"))
                .build();
    }

    @Bean
    public Docket cartApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("장바구니 API")
                .consumes(consumeContentTypes())
                .produces(produceContentTypes())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cart.ui"))
                .paths(PathSelectors.regex("/cart-items.*"))
                .build();
    }

    @Bean
    public Docket orderApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("주문 API")
                .consumes(consumeContentTypes())
                .produces(produceContentTypes())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cart.ui"))
                .paths(PathSelectors.regex("/orders.*"))
                .build();
    }

    @Bean
    public Docket couponApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("쿠폰 API")
                .consumes(consumeContentTypes())
                .produces(produceContentTypes())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cart.ui"))
                .paths(PathSelectors.regex("/coupons.*"))
                .build();
    }

    private Set<String> consumeContentTypes() {
        return Set.of("application/json");
    }

    private Set<String> produceContentTypes() {
        return Set.of("application/json");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("쇼핑 주문 서비스 API")
                .description("우아한테크코스 레벨2 협업 미션")
                .build();
    }
}
