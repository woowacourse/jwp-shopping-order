package cart.auth;

import cart.dao.CredentialDao;
import cart.exception.auth.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private final CredentialDao credentialDao;

    public AuthInterceptor(
            final CredentialDao credentialDao
    ) {
        this.credentialDao = credentialDao;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);

        final Credential credential = BasicAuthorizationParser.parse(authorizationHeader);
        final Credential savedCredential = credentialDao.findByEmail(credential.getEmail())
                .orElseThrow(() -> new AuthenticationException("올바르지 않은 이메일입니다. 입력값: " + credential.getEmail()));

        if (credential.isNotSamePassword(savedCredential)) {
            throw new AuthenticationException();
        }

        request.setAttribute("credential", savedCredential);

        return true;
    }
}
