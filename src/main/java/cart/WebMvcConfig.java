package cart;

import cart.application.MemberService;
import cart.presentation.MemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberService memberService;

    public WebMvcConfig(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .exposedHeaders("*")
                .allowedOrigins(
                        "http://woowacourse-teo.store",
                        "https://woowacourse-teo.store",
                        "https://kyw0716.github.io",
                        "https://gyeongza.github.io"
                ).allowCredentials(true);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberService));
    }
}
