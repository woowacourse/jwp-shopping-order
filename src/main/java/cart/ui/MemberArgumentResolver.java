package cart.ui;

import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import cart.auth.BasicAuthorizationDecoder;
import cart.dao.MemberDao;
import cart.domain.Member;
import cart.exception.AuthenticationException;
import cart.exception.ExceptionType;

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
        if (authorization == null) {
            return null;
        }

        Map.Entry<String, String> emailAndPassword = BasicAuthorizationDecoder.decode(authorization);
        String email = emailAndPassword.getKey();
        String password = emailAndPassword.getValue();

        Member member = memberDao.findByEmail(email);
        if (!member.checkPassword(password)) {
            throw new AuthenticationException(ExceptionType.UNAUTHORIZED);
        }
        return member;
    }
}
