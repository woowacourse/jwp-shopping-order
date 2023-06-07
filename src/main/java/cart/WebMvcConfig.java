package cart;

import cart.member.dao.MemberDao;
import cart.ui.MemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final MemberDao memberDao;
    
    public WebMvcConfig(MemberDao memberDao) {
        this.memberDao = memberDao;
    }
    
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins(
                        "https://kyw0716.github.io",
                        "https://gyeongza.github.io",
                        "http://localhost:3000"
                )
                .exposedHeaders("Location")
                .allowCredentials(true);
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberDao));
    }
}
