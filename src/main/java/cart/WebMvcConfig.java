package cart;

import cart.dao.JdbcTemplateMemberDao;
import cart.ui.MemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final JdbcTemplateMemberDao jdbcTemplateMemberDao;

    public WebMvcConfig(JdbcTemplateMemberDao jdbcTemplateMemberDao) {
        this.jdbcTemplateMemberDao = jdbcTemplateMemberDao;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(jdbcTemplateMemberDao));
    }
}
