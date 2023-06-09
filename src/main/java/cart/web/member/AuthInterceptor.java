package cart.web.member;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.exception.AuthenticationException;
import cart.exception.MemberException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    private final MemberDao memberDao;

    public AuthInterceptor(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        final String[] authenticationHeader = getAuthenticationHeader(request);
        validateHeaderFormat(authenticationHeader);

        String[] credential = getCredential(authenticationHeader);
        Member member = findMemberByCredential(credential);

        if (!member.checkPassword(credential[1])) {
            throw new AuthenticationException.InvalidCredentials();
        }

        request.setAttribute("member", member);
        return true;
    }

    private String[] getAuthenticationHeader(HttpServletRequest request) {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            throw new AuthenticationException.MissingCredentials();
        }

        return authorization.split(" ");
    }

    private void validateHeaderFormat(String[] splitValues) {
        if (splitValues.length != 2) {
            throw new AuthenticationException.InvalidCredentials();
        }

        if (!splitValues[0].equalsIgnoreCase("basic")) {
            throw new AuthenticationException.InvalidScheme();
        }
    }

    private String[] getCredential(String[] authHeader) {
        return Optional.ofNullable(Base64.decodeBase64(authHeader[1]))
                .map(String::new)
                .map(decodedBytes -> decodedBytes.split(":"))
                .filter(credentials -> credentials.length == 2)
                .orElseThrow();
    }

    private Member findMemberByCredential(final String[] credential) {
        return memberDao.getMemberByEmail(credential[0]).orElseThrow(() -> {
            throw new MemberException.NotFound(credential[0]);
        });
    }
}
