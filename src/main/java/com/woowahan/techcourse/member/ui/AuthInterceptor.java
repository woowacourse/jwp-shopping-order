package com.woowahan.techcourse.member.ui;

import com.woowahan.techcourse.cart.exception.AuthenticationException;
import com.woowahan.techcourse.member.application.MemberQueryService;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    private static final int EMAIL_CREDNTIAL_INDEX = 0;
    private static final int PASSWORD_CREDENTIAL_EXIST = 1;
    private final MemberQueryService memberQueryService;

    public AuthInterceptor(MemberQueryService memberQueryService) {
        this.memberQueryService = memberQueryService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreflightRequest(request)) {
            return true;
        }
        validateUserExist(request);
        return true;
    }

    private boolean isPreflightRequest(HttpServletRequest request) {
        return isOptions(request) && hasHeaders(request) && hasMethod(request) && hasOrigin(request);
    }

    private boolean isOptions(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.toString());
    }

    private boolean hasHeaders(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader("Access-Control-Request-Headers"));
    }

    private boolean hasMethod(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader("Access-Control-Request-Method"));
    }

    private boolean hasOrigin(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader("Origin"));
    }

    private void validateUserExist(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String[] credentials = BasicAuthExtractor.extractDecodedCredentials(authorization);
        validateCredentialUserExist(credentials);
    }

    private void validateCredentialUserExist(String[] credentials) {
        String email = credentials[EMAIL_CREDNTIAL_INDEX];
        String password = credentials[PASSWORD_CREDENTIAL_EXIST];
        memberQueryService.findByEmailAndPassword(email, password).orElseThrow(AuthenticationException::new);
    }
}
