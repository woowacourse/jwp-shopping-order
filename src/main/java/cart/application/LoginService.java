package cart.application;

import cart.domain.Member;
import cart.exception.AuthenticationException;
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
            throw new AuthenticationException();
        }

        String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            throw new AuthenticationException();
        }

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);

        String[] credentials = decodedString.split(":");
        String email = credentials[0];
        String password = credentials[1];

        try {
            Member member = memberRepository.findByEmail(email);
            if (!member.checkPassword(password)) {
                throw new AuthenticationException();
            }
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException();
        }
    }
}
