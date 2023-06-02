package com.woowahan.techcourse.member.ui;

import com.woowahan.techcourse.cart.exception.AuthenticationException;
import com.woowahan.techcourse.member.application.MemberQueryService;
import com.woowahan.techcourse.member.domain.Member;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int EMAIL_CREDENTIAL_INDEX = 0;
    private static final int PASSWORD_CREDENTIAL_INDEX = 1;
    private final MemberQueryService memberQueryService;

    public MemberArgumentResolver(MemberQueryService memberQueryService) {
        this.memberQueryService = memberQueryService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String[] credentials = BasicAuthExtractor.extractDecodedCredentials(authorization);
        return findUserByCredentials(credentials);
    }

    private Member findUserByCredentials(String[] credentials) {
        String email = credentials[EMAIL_CREDENTIAL_INDEX];
        String password = credentials[PASSWORD_CREDENTIAL_INDEX];
        return memberQueryService.findByEmailAndPassword(email, password)
                .orElseThrow(AuthenticationException::new);
    }
}
