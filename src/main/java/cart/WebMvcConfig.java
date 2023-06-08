package cart;

import cart.ui.auth.AuthorizationInterceptor;
import cart.ui.auth.BasicAuthorizationProvider;
import cart.ui.auth.MemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final BasicAuthorizationProvider authProvider;

    public WebMvcConfig(BasicAuthorizationProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(authProvider));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*")
            .exposedHeaders("Location");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizationInterceptor(authProvider))
            .addPathPatterns("/points/**", "/orders/**", "/cart-items/**")
            .excludePathPatterns("/", "/settings/**", "/admin/**");
    }
}
