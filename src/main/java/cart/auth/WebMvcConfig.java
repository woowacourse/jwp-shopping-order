package cart.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final MemberArgumentResolver memberArgumentResolver;
    private final AuthInterceptor authInterceptor;

    public WebMvcConfig(final MemberArgumentResolver memberArgumentResolver, final AuthInterceptor authInterceptor) {
        this.memberArgumentResolver = memberArgumentResolver;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/cart-items/**")
                .addPathPatterns("/orders/**")
                .addPathPatterns("/members/**")
                .addPathPatterns("/products/cart-items/**")
                .addPathPatterns("/products/{productId}/cart-items/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }
}
