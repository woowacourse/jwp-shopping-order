package cart.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cart.persistence.member.MemberJdbcRepository;
import cart.ui.MemberArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	private final MemberJdbcRepository memberJdbcRepository;

	public WebMvcConfig(MemberJdbcRepository memberJdbcRepository) {
		this.memberJdbcRepository = memberJdbcRepository;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new MemberArgumentResolver(memberJdbcRepository));
	}

}
