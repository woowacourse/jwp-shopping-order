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
        String[] credentials = AuthExtractor.extract(authorization);
        String email = credentials[0];
        String password = credentials[1];

        return memberQueryService.findByEmailAndPassword(email, password)
                .orElseThrow(AuthenticationException::new);
    }
}
