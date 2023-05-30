package cart.ui.common;

import cart.application.MemberService;
import cart.domain.member.Member;
import cart.ui.controller.dto.response.MemberResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final BasicAuthenticationExtractor basicAuthenticationExtractor;
    private final MemberService memberService;

    public MemberArgumentResolver(BasicAuthenticationExtractor basicAuthenticationExtractor, MemberService memberService) {
        this.basicAuthenticationExtractor = basicAuthenticationExtractor;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberAuth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        MemberAuth memberAuth = basicAuthenticationExtractor.extract(authorization);
        MemberResponse response = memberService.getMemberByEmailAndPassword(memberAuth.getEmail(), memberAuth.getPassword());
        return new Member(response.getId(), response.getEmail(), response.getPassword(), response.getPoint());
    }
}
