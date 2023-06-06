package cart.ui;

import cart.domain.Member;
import cart.exception.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberAuthenticator basicAuthenticator;

    public MemberArgumentResolver(final MemberAuthenticator basicAuthenticator) {
        this.basicAuthenticator = basicAuthenticator;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final String authorization = extractAuthorizationValue(webRequest);

        return basicAuthenticator.findAuthenticatedMember(authorization);
    }

    private String extractAuthorizationValue(final NativeWebRequest webRequest) {
        final String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization != null) {
            return authorization;
        }

        throw new AuthenticationException("인증에 사용되는 헤더 값이 존재하지 않습니다.");
    }
}
