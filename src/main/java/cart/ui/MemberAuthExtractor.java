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
    private final MemberDao memberDao;

    public MemberAuthExtractor(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member extract(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            throw new AuthenticationException("로그인이 필요한 서비스입니다");
        }

        String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            throw new AuthenticationException("인증에 실패했습니다");
        }

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);

        String[] credentials = decodedString.split(":");
        String email = credentials[0];
        String password = credentials[1];

        // 본인 여부 확인
        Optional<Member> optionalMember = memberDao.getMemberByEmail(email);
        final Member member = optionalMember.orElseThrow(() -> new AuthenticationException("아이디 또는 비밀번호가 일치하지 않습니다"));
        if (!member.checkPassword(password)) {
            throw new AuthenticationException("아이디 또는 비밀번호가 일치하지 않습니다");
        }
        return member;
    }
}
