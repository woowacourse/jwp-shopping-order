package com.woowahan.techcourse.member.ui;

import com.woowahan.techcourse.member.domain.Member;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return memberFromRequest(webRequest);
    }

    private Member memberFromRequest(NativeWebRequest webRequest) {
        Object member = webRequest.getAttribute("member", RequestAttributes.SCOPE_REQUEST);
        return (Member) member;
    }
}
