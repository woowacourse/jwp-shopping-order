package cart.config;

import cart.auth.AuthArgumentResolver;
import cart.auth.AuthInterceptor;
import cart.auth.BasicAuthorizationParser;
import cart.auth.CredentialThreadLocal;
import cart.dao.CredentialDao;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final CredentialDao credentialDao;
    private final BasicAuthorizationParser basicAuthorizationParser;
    private final CredentialThreadLocal credentialThreadLocal;

    public WebConfiguration(
            final CredentialDao credentialDao,
            final BasicAuthorizationParser basicAuthorizationParser,
            final CredentialThreadLocal credentialThreadLocal
    ) {
        this.credentialDao = credentialDao;
        this.basicAuthorizationParser = basicAuthorizationParser;
        this.credentialThreadLocal = credentialThreadLocal;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        final AuthInterceptor authInterceptor = new AuthInterceptor(
                credentialDao,
                basicAuthorizationParser,
                credentialThreadLocal
        );
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/cart-items/**")
                .addPathPatterns("/coupons/**")
                .addPathPatterns("/orders/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(credentialThreadLocal));
    }
}
