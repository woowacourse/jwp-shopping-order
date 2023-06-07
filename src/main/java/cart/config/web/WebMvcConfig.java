package cart.config.web;

import cart.config.auth.guard.basic.MemberArgumentResolver;
import cart.config.auth.guard.order.MemberOrderArgumentResolver;
import cart.repository.coupon.CouponRepository;
import cart.repository.member.MemberRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    public WebMvcConfig(final MemberRepository memberRepository, final CouponRepository couponRepository) {
        this.memberRepository = memberRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberRepository));
        resolvers.add(new MemberOrderArgumentResolver(memberRepository, couponRepository));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("Location"); // 노출할 헤더 설정
    }
}
