package cart.application;

import cart.domain.Member;
import cart.exception.AuthenticationException;
import cart.exception.MemberException;
import cart.repository.MemberRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {
    private final MemberRepository memberRepository;

    public LoginService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public void login(final String authorization) {
        if (authorization == null) {
            throw new AuthenticationException.Unauthorized("로그인 정보가 없습니다.");
        }

        String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            throw new AuthenticationException.Unauthorized("로그인 정보가 없습니다.");
        }

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);

        String[] credentials = decodedString.split(":");
        String email = credentials[0];
        String password = credentials[1];

        try {
            Member member = memberRepository.findByEmail(email);
            member.checkPassword(password);
        } catch (MemberException.NoExist exception) {
            throw new AuthenticationException.LoginFail("로그인 정보가 잘못되었습니다.");
        }
    }
}
