package cart.config;

import cart.application.AuthService;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemberBasicAuthInterceptor implements HandlerInterceptor {

    public static final String BASIC = "Basic";

    private final AuthService authService;

    public MemberBasicAuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String credential = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (credential == null) {
            throw new CartException(ErrorCode.PRE_AUTHENTICATION);
        }

        if (isNotBasicAuth(credential)) {
            throw new CartException(ErrorCode.PRE_AUTHENTICATION);
        }

        String authValueWithBase64Encoding = credential.substring(BASIC.length()).trim();
        String auth = new String(Base64Utils.decodeFromString(authValueWithBase64Encoding));

        String[] emailAndPasswordWithDecryption = auth.split(":");
        String email = emailAndPasswordWithDecryption[0];
        String password = emailAndPasswordWithDecryption[1];
        authService.validateMember(email, password);
        return true;
    }

    private boolean isNotBasicAuth(String credential) {
        return !credential.startsWith(BASIC);
    }
}
