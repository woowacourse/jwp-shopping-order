package cart.common.config;

import cart.common.auth.MemberNameArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberNameArgumentResolver memberNameArgumentResolver;

    public WebMvcConfig(MemberNameArgumentResolver memberNameArgumentResolver) {
        this.memberNameArgumentResolver = memberNameArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberNameArgumentResolver);
    }
}
