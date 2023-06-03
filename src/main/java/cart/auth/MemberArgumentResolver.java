package cart.auth;

import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberDao memberDao;

    public MemberArgumentResolver(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            return null;
        }

        final String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            return null;
        }

        final byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        final String decodedString = new String(decodedBytes);

        final String[] credentials = decodedString.split(":");
        final String email = credentials[0];
        final String password = credentials[1];

        // 본인 여부 확인
        final Member member = memberDao.getMemberByEmail(email);
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }
        return member;
    }
}
