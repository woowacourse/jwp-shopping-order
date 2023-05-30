package cart.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (accessToken == null) {
            throw new AuthorizationException("인증 정보가 존재하지 않습니다.");
        }

        return true;
    }
}
