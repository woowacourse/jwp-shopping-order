package cart.configuration;

import cart.authentication.AuthenticationInterceptor;
import cart.authentication.AuthenticationMemberConverter;
import cart.authentication.BasicAuthorizationExtractor;
import cart.authentication.MemberStore;
import cart.configuration.resolver.CheckoutArgumentResolver;
import cart.configuration.resolver.MemberArgumentResolver;
import cart.repository.MemberRepository;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;

    public WebMvcConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("Authorization")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowCredentials(false)
                .exposedHeaders("Location");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberStore()));
        resolvers.add(new CheckoutArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        BasicAuthorizationExtractor extractor = new BasicAuthorizationExtractor();
        AuthenticationMemberConverter memberConverter = new AuthenticationMemberConverter(memberRepository);
        AuthenticationInterceptor authInterceptor = new AuthenticationInterceptor(extractor, memberConverter,
                memberStore());

        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/cart-items/**")
                .addPathPatterns("/orders/**");
    }

    @Bean
    public MemberStore memberStore() {
        return new MemberStore();
    }
}
