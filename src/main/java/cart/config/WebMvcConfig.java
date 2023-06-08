package cart.config;

import cart.application.MemberService;
import cart.config.auth.MemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final List<String> MAPPING_URLS = List.of(
            "/products/**",
            "/cart-items/**",
            "/orders/**",
            "/coupons/**");
    private static final String[] ALLOWED_ORIGINS = {
            "https://feb-dain.github.io",
            "https://cruelladevil.github.io",
            "http://localhost:3000"};

    private final MemberService memberService;

    public WebMvcConfig(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        for (final String mappingUrl : MAPPING_URLS) {
            registry.addMapping(mappingUrl)
                    .allowedOrigins(ALLOWED_ORIGINS)
                    .allowedMethods(
                            HttpMethod.GET.name(),
                            HttpMethod.POST.name(),
                            HttpMethod.PUT.name(),
                            HttpMethod.PATCH.name(),
                            HttpMethod.DELETE.name(),
                            HttpMethod.OPTIONS.name())
                    .allowedHeaders("*")
                    .exposedHeaders("Location")
                    .allowCredentials(true);
        }
    }
    
    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberService));
    }
}
