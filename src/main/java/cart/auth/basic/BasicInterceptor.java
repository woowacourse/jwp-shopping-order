package cart.auth.basic;


import cart.exception.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class BasicInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader(AUTHORIZATION);

        if (Objects.isNull(authorization)) {
            throw new AuthenticationException("사용자 정보가 존재하지 않습니다.");
        }

        String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            throw new AuthenticationException("basic 인증 도중에 오류가 발생하였습니다.");
        }

        return true;
    }
}
