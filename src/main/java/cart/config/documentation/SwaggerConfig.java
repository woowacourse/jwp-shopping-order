package cart.config.documentation;

import cart.domain.Member;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

@Component
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        return createDocket("상품", "/products.*");

    }

    @Bean
    public Docket cartApi() {
        return createDocket("장바구니", "/cart-items.*");

    }

    @Bean
    public Docket orderApi() {
        return createDocket("주문", "/orders.*");

    }

    @Bean
    public Docket couponApi() {
        return createDocket("쿠폰", "/coupons.*");
    }

    private Docket createDocket(final String groupName, final String pathRegex) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .consumes(consumeContentTypes())
                .produces(produceContentTypes())
                .securitySchemes(List.of(new BasicAuth("basicAuth")))
                .securityContexts(Collections.singletonList(securityContext()))
                .ignoredParameterTypes(Member.class)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cart.ui"))
                .paths(PathSelectors.regex(pathRegex))
                .build();
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(null)
                .clientSecret(null)
                .realm(null)
                .appName(null)
                .scopeSeparator(",")
                .additionalQueryStringParams(null)
                .useBasicAuthenticationWithAccessCodeGrant(false)
                .build();
    }

    public SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(
                        Collections.singletonList(SecurityReference.builder()
                                .reference("basicAuth")
                                .scopes(new AuthorizationScope[0])
                                .build()))
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
