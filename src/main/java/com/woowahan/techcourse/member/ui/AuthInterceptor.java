package com.woowahan.techcourse.member.ui;

import com.woowahan.techcourse.cart.exception.AuthenticationException;
import com.woowahan.techcourse.member.application.MemberQueryService;
import com.woowahan.techcourse.member.domain.Member;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    private final MemberQueryService memberQueryService;

    public AuthInterceptor(MemberQueryService memberQueryService) {
        this.memberQueryService = memberQueryService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreflightRequest(request)) {
            return true;
        }
        Member member = findMember(request);
        request.setAttribute("member", member);
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

    private Member findMember(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        AuthCredentials credentials = BasicAuthExtractor.extractDecodedCredentials(authorization);
        return memberQueryService.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword())
                .orElseThrow(AuthenticationException::new);
    }
}
