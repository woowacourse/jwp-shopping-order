package cart.config;

import cart.auth.AuthArgumentResolver;
import cart.auth.AuthContext;
import cart.auth.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final AuthContext authContext;

    public WebMvcConfiguration(final AuthInterceptor authInterceptor, final AuthContext authContext) {
        this.authInterceptor = authInterceptor;
        this.authContext = authContext;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(authContext));
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(List.of("/cart-items/**", "/orders/**", "/points/**"));
    }
}
