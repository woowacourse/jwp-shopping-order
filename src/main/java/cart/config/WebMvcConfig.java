package cart.config;

import cart.application.AuthService;
import cart.dao.member.MemberDao;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberBasicAuthInterceptor memberBasicAuthInterceptor;
    private final MemberArgumentResolver memberArgumentResolver;

    public WebMvcConfig(AuthService authService, MemberDao memberDao) {
        this.memberBasicAuthInterceptor = new MemberBasicAuthInterceptor(authService);
        this.memberArgumentResolver = new MemberArgumentResolver(memberDao);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memberBasicAuthInterceptor)
                .addPathPatterns("/cart-items/**", "/users/**", "/orders/**")
                .order(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }
}
