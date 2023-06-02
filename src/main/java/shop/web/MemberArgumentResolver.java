package shop.web;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shop.domain.member.Member;
import shop.domain.repository.MemberRepository;
import shop.exception.AuthenticationException;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberRepository memberRepository;

    public MemberArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            throw new AuthenticationException("인증 정보가 존재하지 않습니다.");
        }

        String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            throw new AuthenticationException("인증 정보가 존재하지 않습니다.");
        }

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);

        String[] credentials = decodedString.split(":");
        String name = credentials[0];
        String password = credentials[1];

        // 본인 여부 확인

        Member member = memberRepository.findByName(name);
        if (!member.checkPassword(password)) {
            throw new AuthenticationException("인증되지 않은 사용자입니다.");
        }

        return member;
    }
}
