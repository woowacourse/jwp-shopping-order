package cart.ui.auth;

import cart.domain.Member;
import cart.exception.authexception.AuthenticationException;
import cart.repository.MemberRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthorizationProvider {

    private static final String BASIC_TYPE = "Basic";
    private static final String DELIMITER = ":";
    private static final int EMAIL_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;
    private final MemberRepository memberRepository;

    public BasicAuthorizationProvider(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public AuthInfo resolveAuthInfo(String authHeader) {
        if (authHeader == null) {
            throw new AuthenticationException();
        }

        if ((authHeader.toLowerCase().startsWith(BASIC_TYPE.toLowerCase()))) {
            String authValue = authHeader.substring(BASIC_TYPE.length()).trim();
            byte[] decodedBytes = Base64.decodeBase64(authValue);
            String decodedString = new String(decodedBytes);
            String[] credentials = decodedString.split(DELIMITER);
            return new AuthInfo(credentials[EMAIL_INDEX], credentials[PASSWORD_INDEX]);
        }
        throw new AuthenticationException();
    }

    public boolean isAbleToLogin(AuthInfo authInfo) {
        return memberRepository.isEmailAndPasswordExist(authInfo.getEmail(),
            authInfo.getPassword());
    }

    public Member resolveMember(AuthInfo authInfo) {
        return memberRepository.findByEmail(authInfo.getEmail());
    }
}
