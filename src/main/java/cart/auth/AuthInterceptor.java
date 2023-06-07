package cart.auth;

import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final MemberDao memberDao;

    public AuthInterceptor(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            throw new AuthenticationException("헤더에 토큰이 존재하지 않습니다");
        }

        final String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            throw new AuthenticationException("Basic 토큰이 아닙니다");
        }

        final byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        final String decodedString = new String(decodedBytes);

        final String[] credentials = decodedString.split(":");
        if (credentials.length != 2) {
            throw new AuthenticationException("이메일:비밀번호 형식이 아닙니다");
        }

        final String email = credentials[0];
        final String password = credentials[1];

        final Member member = memberDao.getMemberByEmail(email);
        if (!member.checkPassword(password)) {
            throw new AuthenticationException("로그인 정보가 불일치합니다");
        }
        request.setAttribute("member", member);

        return member != null;
    }
}
