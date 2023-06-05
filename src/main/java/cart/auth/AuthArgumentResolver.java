package cart.auth;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final CredentialThreadLocal credentialThreadLocal;

    public AuthArgumentResolver(final CredentialThreadLocal credentialThreadLocal) {
        this.credentialThreadLocal = credentialThreadLocal;
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
        final Credential credential = credentialThreadLocal.get();
        credentialThreadLocal.clear();
        return credential;
    }
}
