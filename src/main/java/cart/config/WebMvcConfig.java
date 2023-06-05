package cart.config;

import cart.dao.AuthDao;
import cart.auth.MemberArgumentResolver;
import cart.config.LoggingInterceptorHandler;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthDao authDao;
    private final LoggingInterceptorHandler loggingInterceptorHandler;

    public WebMvcConfig(final AuthDao authDao, final LoggingInterceptorHandler loggingInterceptorHandler) {
        this.authDao = authDao;
        this.loggingInterceptorHandler = loggingInterceptorHandler;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptorHandler);
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(authDao));
    }
}
