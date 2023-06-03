package cart.auth;

import cart.dao.CredentialDao;
import cart.exception.auth.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthArgumentResolver implements HandlerMethodArgumentResolver {


    private final CredentialDao credentialDao;
    private final BasicAuthorizationParser basicAuthorizationParser;

    public AuthArgumentResolver(final CredentialDao credentialDao, final BasicAuthorizationParser basicAuthorizationParser) {
        this.credentialDao = credentialDao;
        this.basicAuthorizationParser = basicAuthorizationParser;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        final boolean hasParameterAnnotation = parameter.hasParameterAnnotation(Auth.class);
        final boolean hasCredentialType = Credential.class.isAssignableFrom(parameter.getParameterType());
        return hasParameterAnnotation && hasCredentialType;
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final String authorizationHeader = webRequest.getHeader("Authorization");
        final Credential credential = basicAuthorizationParser.parse(authorizationHeader);
        return credentialDao.findByEmail(credential.getEmail())
                .orElseThrow(() -> new AuthenticationException("올바르지 않은 이메일입니다. 입력값: " + credential.getEmail()));

    }
}
