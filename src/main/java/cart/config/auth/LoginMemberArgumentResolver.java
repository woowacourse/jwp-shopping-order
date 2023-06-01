package cart.config.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import cart.application.member.MemberQueryService;
import cart.config.auth.dto.AuthInfo;
import cart.config.auth.dto.AuthMember;
import cart.error.exception.AuthenticationException;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

	private final BasicAuthorizationExtractor basicAuthorizationExtractor;
	private final MemberQueryService memberQueryService;

	public LoginMemberArgumentResolver(final BasicAuthorizationExtractor basicAuthorizationExtractor,
		final MemberQueryService memberQueryService) {
		this.basicAuthorizationExtractor = basicAuthorizationExtractor;
		this.memberQueryService = memberQueryService;
	}

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {
		boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginMember.class) != null;
		boolean isUserClass = AuthMember.class.equals(parameter.getParameterType());

		return isLoginUserAnnotation && isUserClass;
	}

	@Override
	public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
		final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
		final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

		validateRequest(request);

		return createAuthMember(request);
	}

	private void validateRequest(final HttpServletRequest request) {
		if (request == null) {
			throw new AuthenticationException.Unauthorized();
		}
	}

	private AuthMember createAuthMember(final HttpServletRequest request) {
		final AuthInfo authInfo = basicAuthorizationExtractor.extract(request);
		return new AuthMember(memberQueryService.checkLoginMember(authInfo));
	}

}
