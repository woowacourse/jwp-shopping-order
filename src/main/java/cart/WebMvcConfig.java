package cart;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cart.dao.MemberDao;
import cart.ui.MemberArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	private final MemberDao memberDao;

	public WebMvcConfig(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new MemberArgumentResolver(memberDao));
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("*")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTION")
			.allowedHeaders("*")
			.exposedHeaders("Location")
			.allowCredentials(true);
	}
}
