package cart;

import cart.application.service.AuthService;
import cart.ui.MemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthService authService;

    public WebMvcConfig(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(authService));
    }
}
