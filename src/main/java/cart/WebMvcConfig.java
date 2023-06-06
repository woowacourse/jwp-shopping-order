package cart;

import cart.controller.BasicTokenExtractor;
import cart.controller.MemberArgumentResolver;
import cart.domain.member.repository.MemberRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;
    private final BasicTokenExtractor basicTokenExtractor;

    public WebMvcConfig(MemberRepository memberRepository, BasicTokenExtractor basicTokenExtractor) {
        this.memberRepository = memberRepository;
        this.basicTokenExtractor = basicTokenExtractor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberRepository, basicTokenExtractor));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .exposedHeaders(HttpHeaders.LOCATION)
                .allowCredentials(true);
    }
}
