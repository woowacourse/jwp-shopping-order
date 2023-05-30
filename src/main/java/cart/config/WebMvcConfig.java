package cart.config;

import cart.application.MemberService;
import cart.ui.BasicAuthInterceptor;
import cart.ui.BasicAuthenticationExtractor;
import cart.ui.MemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberService memberService;

    public WebMvcConfig(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BasicAuthInterceptor(new BasicAuthenticationExtractor(), memberService))
                .addPathPatterns("/cart-items/**", "/members/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(new BasicAuthenticationExtractor(), memberService));
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders(HttpHeaders.LOCATION)
                .maxAge(86400);
    }
}
