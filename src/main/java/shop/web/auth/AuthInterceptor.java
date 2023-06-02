package shop.web.auth;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor extends HandlerInterceptorAdapter {
    private static final int INDEX_OF_NAME = 0;
    private static final int INDEX_OF_PASSWORD = 1;
    private static final String DELIMITER = ":";

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        BasicAuthorizationExtractor extractor = BasicAuthorizationExtractor.getInstance();

        String extractedHeader = extractor.extract(request);

        String[] credentials = extractedHeader.split(DELIMITER);
        String name = credentials[INDEX_OF_NAME];
        String password = credentials[INDEX_OF_PASSWORD];

        authService.authenticate(name, password);

        return super.preHandle(request, response, handler);
    }
}
