package cart;

import cart.dao.MemberDao;
import cart.domain.*;
import cart.ui.MemberArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final int LIMIT = 10;

    private final MemberDao memberDao;

    public WebMvcConfig(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberDao));
    }

    @Bean
    public OrderPage getOrderPage() {
        return new OrderPage(LIMIT);
    }

    @Bean
    public PointExpirePolicy getPointExpirePolicy() {
        return new OrderPointExpirePolicy();
    }

    @Bean
    public PointAccumulationPolicy getPointAccumulationPolicy() {
        return new OrderPointAccumulationPolicy(getPointExpirePolicy());
    }
}
