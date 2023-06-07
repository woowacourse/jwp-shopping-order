package cart.presentation;

import cart.exception.AuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthValidateInterceptor implements HandlerInterceptor {

    private static final String AUTH_TYPE = "basic";
    private static final int AUTH_TYPE_INDEX = 0;
    private static final int AUTH_VALUE_INDEX = 1;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        validateNullOrEmpty(authorization);

        String[] splitAuthorization = authorization.split(" ");
        String authType = splitAuthorization[AUTH_TYPE_INDEX];
        String authValue = splitAuthorization[AUTH_VALUE_INDEX];

        validateAuthType(authType);
        validateNullOrEmpty(authType);
        validateNullOrEmpty(authValue);

        return true;
    }

    private void validateAuthType(final String authType) {
        if (!authType.equalsIgnoreCase(AUTH_TYPE)) {
            throw new AuthenticationException("인증 타입이 잘못되었습니다.");
        }
    }

    private void validateNullOrEmpty(final String value) {
        if (value == null || value.isEmpty()) {
            throw new AuthenticationException("인증 정보가 필요합니다.");
        }
    }
}
