package cart.ui.auth;

import cart.exception.authexception.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private final BasicAuthorizationProvider authProvider;

    public AuthorizationInterceptor(BasicAuthorizationProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        String authHeader = request.getHeader(AUTHORIZATION);
        AuthInfo authInfo = authProvider.resolveAuthInfo(authHeader);
        if (authProvider.isAbleToLogin(authInfo)) {
            return true;
        }
        throw new AuthenticationException();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex)
        throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
