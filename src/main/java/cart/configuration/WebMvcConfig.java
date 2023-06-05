package cart.configuration;

import cart.authentication.AuthenticationInterceptor;
import cart.configuration.converter.CartItemIdsConverter;
import cart.configuration.resolver.MemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authInterceptor;
    private final MemberArgumentResolver memberArgumentResolver;
    private final CartItemIdsConverter cartItemIdsConverter;

    public WebMvcConfig(
            AuthenticationInterceptor authInterceptor,
            MemberArgumentResolver memberArgumentResolver,
            CartItemIdsConverter cartItemIdsConverter
    ) {
        this.authInterceptor = authInterceptor;
        this.memberArgumentResolver = memberArgumentResolver;
        this.cartItemIdsConverter = cartItemIdsConverter;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("Authorization")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowCredentials(false)
                .exposedHeaders("Location");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/cart-items/**")
                .addPathPatterns("/orders/**");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(cartItemIdsConverter);
    }
}
