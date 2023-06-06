package cart.config;

import cart.application.service.member.MemberReadService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final MemberReadService memberReadService;

    public WebMvcConfig(MemberReadService memberReadService) {
        this.memberReadService = memberReadService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberReadService));
    }
}
