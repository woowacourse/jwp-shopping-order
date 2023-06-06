package com.woowahan.techcourse.common.config;

import com.woowahan.techcourse.member.application.MemberQueryService;
import com.woowahan.techcourse.member.ui.AuthInterceptor;
import com.woowahan.techcourse.member.ui.MemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberQueryService memberQueryService;

    public WebMvcConfig(MemberQueryService memberQueryService) {
        this.memberQueryService = memberQueryService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOriginPatterns("*")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(memberQueryService))
                .addPathPatterns("/cart-items/**", "/orders/**", "/members/**", "/orders");
    }
}
