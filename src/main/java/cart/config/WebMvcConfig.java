package cart.config;

import cart.domain.respository.member.MemberRepository;
import cart.infrastructure.JwtTokenProvider;
import cart.ui.TokenAuthMemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public WebMvcConfig(final MemberRepository memberRepository, final JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://218.39.176.142:3000",
                "https://woowasplit.shop/",
                "https://react-shopping-cart-woowa.netlify.app/",
                "https://react-shopping-cart-prod-6izahtdpl-shackstack.vercel.app/",
                "https://shackstack-tiffany.vercel.app/"
            )
            .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTION")
            .allowedHeaders("*")
            .allowCredentials(true);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new TokenAuthMemberArgumentResolver(jwtTokenProvider, memberRepository));
    }
}
