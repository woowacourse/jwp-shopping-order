package cart.configuration;

import cart.authentication.AuthenticationInterceptor;
import cart.authentication.MemberStore;
import cart.configuration.resolver.CheckoutArgumentResolver;
import cart.configuration.resolver.MemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authInterceptor;
    private final MemberStore memberStore;

    public WebMvcConfig(AuthenticationInterceptor authInterceptor, MemberStore memberStore) {
        this.authInterceptor = authInterceptor;
        this.memberStore = memberStore;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("Authorization")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowCredentials(false)
                .exposedHeaders("Location");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberStore));
        resolvers.add(new CheckoutArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/cart-items/**")
                .addPathPatterns("/orders/**");
    }
}
