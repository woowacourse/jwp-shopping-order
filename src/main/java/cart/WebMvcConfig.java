package cart;

import cart.ui.MemberArgumentResolver;
import cart.ui.MemberAuthExtractor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final MemberAuthExtractor memberAuthExtractor;

    public WebMvcConfig(MemberAuthExtractor memberAuthExtractor) {
        this.memberAuthExtractor = memberAuthExtractor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberAuthExtractor));
    }
}
