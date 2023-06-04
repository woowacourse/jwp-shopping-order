package cart.auth.basic;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.exception.AuthenticationException;
import cart.exception.MemberException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class BasicMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private static final int USERNAME_AND_PASSWORD_SIZE = 2;
    private static final int USERNAME = 0;
    private static final int PASSWORD = 1;
    private final MemberDao memberDao;

    public BasicMemberArgumentResolver(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        List<String> usernameAndPassword = BasicDecoder.decode(webRequest.getHeader(AUTHORIZATION));
        if (usernameAndPassword.size() != USERNAME_AND_PASSWORD_SIZE) {
            throw new AuthenticationException("basic 인증 도중에 예외가 발생하였습니다.");
        }

        String email = usernameAndPassword.get(USERNAME);
        String password = usernameAndPassword.get(PASSWORD);
        Member member = memberDao.getByMemberEmail(email)
                .orElseThrow(MemberException.NotFound::new)
                .toDomain();

        if (!member.isSamePassword(password)) {
            throw new AuthenticationException("회원 정보가 일치하지 않습니다.");
        }

        return member;
    }
}
