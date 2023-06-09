package cart.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cart.config.auth.LoginMemberArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final LoginMemberArgumentResolver loginMemberArgumentResolver;

	public WebMvcConfig(final LoginMemberArgumentResolver loginMemberArgumentResolver) {
		this.loginMemberArgumentResolver = loginMemberArgumentResolver;
	}

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginMemberArgumentResolver);
	}

}
