package cart.auth;

import cart.dao.MemberDao;
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
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberDao memberDao;
    private final BasicAuthorizationExtractor basicAuthorizationExtractor;

    public AuthArgumentResolver(
            final MemberDao memberDao,
            final BasicAuthorizationExtractor basicAuthorizationExtractor
    ) {
        this.memberDao = memberDao;
        this.basicAuthorizationExtractor = basicAuthorizationExtractor;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) {
        String header = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        AuthInfo authInfo = basicAuthorizationExtractor.extract(header);

        Member member = memberDao.getMemberByEmail(authInfo.getEmail());
        if (!member.checkPassword(authInfo.getPassword())) {
            throw new AuthenticationException();
        }
        return member;
    }
}
