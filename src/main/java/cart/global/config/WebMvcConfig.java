package cart.global.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthMemberInterceptor authMemberInterceptor;

    public WebMvcConfig(AuthMemberInterceptor authMemberInterceptor) {
        this.authMemberInterceptor = authMemberInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authMemberInterceptor)
                .addPathPatterns("/cart-items/**", "/members/**", "/orders/**",
                        "/products/{id}/cart-items", "/products/cart-items");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver());
    }
}
