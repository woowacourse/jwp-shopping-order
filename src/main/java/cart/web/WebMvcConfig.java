package cart.web;

import cart.dao.MemberDao;
import cart.web.member.AuthInterceptor;
import cart.web.member.MemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final MemberDao memberDao;

    public WebMvcConfig(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(memberDao))
                .addPathPatterns("/cart-items/**", "/orders/**", "/total-cart-price/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .exposedHeaders(HttpHeaders.LOCATION)
                .allowedHeaders("*");
    }
}
