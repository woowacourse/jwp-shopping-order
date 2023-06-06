package cart.config;

import cart.auth.AuthArgumentResolver;
import cart.auth.AuthInterceptor;
import cart.dao.CredentialDao;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final CredentialDao credentialDao;

    public WebConfiguration(
            final CredentialDao credentialDao
    ) {
        this.credentialDao = credentialDao;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        final AuthInterceptor authInterceptor = new AuthInterceptor(
                credentialDao
        );
        registry.addInterceptor(authInterceptor).addPathPatterns("/cart-items/**");
        registry.addInterceptor(authInterceptor).addPathPatterns("/orders/**");
        registry.addInterceptor(authInterceptor).addPathPatterns("/coupons/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver());
    }
}
