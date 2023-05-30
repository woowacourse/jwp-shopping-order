package com.woowahan.techcourse.common.config;

import com.woowahan.techcourse.cart.ui.MemberArgumentResolver;
import com.woowahan.techcourse.member.application.MemberQueryService;
import com.woowahan.techcourse.member.dao.MemberDao;
import com.woowahan.techcourse.member.ui.resolver.MemberIdResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberDao memberDao;
    private final MemberQueryService memberQueryService;

    public WebMvcConfig(MemberDao memberDao, MemberQueryService memberQueryService) {
        this.memberDao = memberDao;
        this.memberQueryService = memberQueryService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberDao));
        resolvers.add(new MemberIdResolver(memberQueryService));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOriginPatterns("*")
                .allowCredentials(true);
    }

}
