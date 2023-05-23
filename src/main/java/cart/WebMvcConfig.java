package cart;

import cart.dao.MemberDao;
import cart.ui.LoggingRequestUrlInterceptor;
import cart.ui.MemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final MemberDao memberDao;
    private final LoggingRequestUrlInterceptor loggingRequestUrlInterceptor;

    public WebMvcConfig(MemberDao memberDao, final LoggingRequestUrlInterceptor loggingRequestUrlInterceptor) {
        this.memberDao = memberDao;
        this.loggingRequestUrlInterceptor = loggingRequestUrlInterceptor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(loggingRequestUrlInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberDao));
    }
}
