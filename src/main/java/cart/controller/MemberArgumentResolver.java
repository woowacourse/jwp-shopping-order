package cart.controller;

import cart.domain.member.Member;
import cart.domain.member.repository.MemberRepository;
import cart.dto.AuthMember;
import cart.exception.AuthenticationException;
import cart.exception.ExceptionType;
import cart.exception.MemberException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;
    private final BasicTokenExtractor basicTokenExtractor;

    public MemberArgumentResolver(MemberRepository memberRepository, BasicTokenExtractor basicTokenExtractor) {
        this.memberRepository = memberRepository;
        this.basicTokenExtractor = basicTokenExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthPrincipal.class) &&
                parameter.getParameterType().equals(AuthMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        validateAuthorizationHeader(authorization);
        String[] credentials = basicTokenExtractor.decode(authorization);
        String email = credentials[0];
        String password = credentials[1];

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ExceptionType.INVALID_LOGIN_INFO));
        if (!member.checkPassword(password)) {
            throw new MemberException(ExceptionType.INVALID_LOGIN_INFO);
        }
        return new AuthMember(member.getId(), member.getEmail());
    }

    private void validateAuthorizationHeader(String authorization) {
        if (authorization == null) {
            throw new AuthenticationException("Authorization 헤더가 없습니다.");
        }
    }
}
