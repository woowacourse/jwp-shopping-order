package cart.config.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import cart.config.auth.dto.AuthInfo;
import cart.config.auth.dto.BasicAuthInfo;
import cart.error.exception.UnauthorizedException;

@Component
public class BasicAuthorizationExtractor implements AuthorizationExtractor<AuthInfo> {

	@Override
	public AuthInfo extract(final HttpServletRequest request) {
		String authorization = request.getHeader(AUTHORIZATION);

		validateAuthorization(authorization);

		return BasicAuthInfo.from(authorization);
	}

	private static void validateAuthorization(final String authorization) {
		if (authorization == null) {
			throw new UnauthorizedException.Null();
		}
	}
}
