package shop;

import shop.domain.repository.MemberRepository;
import shop.presentation.MemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final MemberRepository memberRepository;

    public WebMvcConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberRepository));
    }

    // TODO: 2023-05-28 localhost8080 빼도 될 듯
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://monumental-kleicha-ad648a.netlify.app/", "http://localhost:9000", "http://localhost:3000", "http://localhost:8080", "https://jourzura2.kro.kr")
                .allowedMethods("*");
    }
}
