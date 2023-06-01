package cart.configuration.resolver;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.exception.AuthenticationException.ForbiddenMember;
import cart.exception.AuthenticationException.InvalidTokenFormat;
import cart.exception.AuthenticationException.LoginFailed;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BASIC_TYPE = "Basic";
    private static final String DELIMITER = ":";
    private static final int EMAIL_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;

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
        String header = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        validateAuthorizationHeader(header);

        String authHeaderValue = header.substring(BASIC_TYPE.length()).trim();
        String decoded = new String(Base64.decodeBase64(authHeaderValue));

        String[] credentials = decoded.split(DELIMITER);

        try {
            String email = credentials[EMAIL_INDEX];
            String password = credentials[PASSWORD_INDEX];

            Member member = memberDao.findByEmail(email)
                    .orElseThrow(LoginFailed::new);

            if (!member.checkPassword(password)) {
                throw new LoginFailed();
            }
            return member;
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new InvalidTokenFormat("토큰 형식이 올바르지 않습니다.", ex);
        }
    }

    private void validateAuthorizationHeader(String header) {
        if (header == null) {
            throw new ForbiddenMember();
        }
        if (checkNonBasicType(header)) {
            throw new InvalidTokenFormat("올바른 인증 방식이 아닙니다.");
        }
    }

    private boolean checkNonBasicType(String header) {
        return !header.toLowerCase().startsWith(BASIC_TYPE.toLowerCase());
    }
}
