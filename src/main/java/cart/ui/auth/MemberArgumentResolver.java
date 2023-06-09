package cart.ui.auth;

import cart.domain.Member;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION = "Authorization";
    private final BasicAuthorizationProvider authProvider;

    public MemberArgumentResolver(BasicAuthorizationProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authHeader = webRequest.getHeader(AUTHORIZATION);
        AuthInfo authInfo = authProvider.resolveAuthInfo(authHeader);
        return authProvider.resolveMember(authInfo);
    }
}
