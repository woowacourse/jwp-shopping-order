package cart.ui.auth;

import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.exception.AuthenticationException;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberDao memberDao;

    public MemberArgumentResolver(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        Credentials credentials = BasicAuthorizationExtractor.extract(authorization);
        validateHas(credentials);

        // 본인 여부 확인
        return memberDao.getMemberByEmail(credentials.getEmail())
                .filter(it -> it.checkPassword(credentials.getPassword()))
                .orElseThrow(AuthenticationException::new);
    }

    private void validateHas(Credentials credentials) {
        if (Objects.isNull(credentials)) {
            throw new AuthenticationException();
        }
    }
}
