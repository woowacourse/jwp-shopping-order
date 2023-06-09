package cart.ui;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.exception.AuthenticationException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class MemberAuthExtractor {

    private static final String BASIC_AUTH_SCHEME = "basic";
    private static final String BASE64_CREDENTIALS_DELIMITER = ":";
    private static final int AUTH_HEADER_SCHEME_INDEX = 0;
    private static final String AUTH_VALUE_DELIMITER = " ";

    private final MemberDao memberDao;

    public MemberAuthExtractor(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member extract(HttpServletRequest request) {
        String encodedAuthValue = getEncodedAuthValue(request);
        String[] credentials = decodeIntoCredentials(encodedAuthValue);

        String email = credentials[0];
        String password = credentials[1];

        return findMember(email, password);
    }

    private String getEncodedAuthValue(HttpServletRequest request) {
        String authorization = getAuthHeaderValue(request);
        String[] authHeader = authorization.split(AUTH_VALUE_DELIMITER);

        validateBasicScheme(authHeader);

        return authHeader[1];
    }

    private String getAuthHeaderValue(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            throw new AuthenticationException("로그인이 필요한 서비스입니다");
        }
        return authorization;
    }

    private void validateBasicScheme(String[] authHeader) {
        if (!authHeader[AUTH_HEADER_SCHEME_INDEX].equalsIgnoreCase(BASIC_AUTH_SCHEME)) {
            throw new AuthenticationException("인증에 실패했습니다");
        }
    }

    private String[] decodeIntoCredentials(String encodedAuthValue) {
        byte[] decodedBytes = Base64.decodeBase64(encodedAuthValue);
        String decodedString = new String(decodedBytes);

        return decodedString.split(BASE64_CREDENTIALS_DELIMITER);
    }

    private Member findMember(String email, String password) {
        Optional<Member> optionalMember = memberDao.getMemberByEmail(email);
        final Member member = optionalMember.orElseThrow(() -> new AuthenticationException("아이디 또는 비밀번호가 일치하지 않습니다"));
        if (!member.checkPassword(password)) {
            throw new AuthenticationException("아이디 또는 비밀번호가 일치하지 않습니다");
        }
        return member;
    }
}
