package cart.auth;

import cart.dao.CredentialDao;
import cart.exception.auth.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private final CredentialDao credentialDao;
    private final BasicAuthorizationParser basicAuthorizationParser;

    public AuthInterceptor(
            final CredentialDao credentialDao,
            final BasicAuthorizationParser basicAuthorizationParser
    ) {
        this.credentialDao = credentialDao;
        this.basicAuthorizationParser = basicAuthorizationParser;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);

        final Credential credential = basicAuthorizationParser.parse(authorizationHeader);
        final Credential savedCredential = credentialDao.findByEmail(credential.getEmail())
                .orElseThrow(() -> new AuthenticationException("올바르지 않은 이메일입니다. 입력값: " + credential.getEmail()));

        if (credential.isNotSamePassword(savedCredential)) {
            throw new AuthenticationException();
        }

        return true;
    }
}
