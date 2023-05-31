package cart.ui;

import cart.domain.Member;
import cart.exception.AuthenticationException;
import cart.repository.MemberRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BASIC_AUTH_PREFIX = "Basic ";

    private final MemberRepository memberRepository;

    public MemberArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthPrincipal.class) &&
                parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        validateAuthorizationHeader(authorization);

        String[] credentials = decode(authorization);
        String email = credentials[0];
        String password = credentials[1];

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("잘못된 로그인 정보입니다."));
        if (!member.checkPassword(password)) {
            throw new AuthenticationException("비밀번호가 틀렸습니다.");
        }
        return member;
    }

    private String[] decode(String authorization) {
        String token = authorization.replace(BASIC_AUTH_PREFIX, "");
        String decodeToken = decodeToken(token);
        return decodeToken.split(":");
    }

    private String decodeToken(String token) {
        try {
            return new String(Base64.decodeBase64(token));
        } catch (IllegalStateException e) {
            throw new AuthenticationException("잘못된 토큰입니다.");
        }
    }

    private void validateAuthorizationHeader(String authorization) {
        if (authorization == null) {
            throw new AuthenticationException("Authorization 헤더가 없습니다.");
        }
        if (!authorization.startsWith(BASIC_AUTH_PREFIX)) {
            throw new AuthenticationException("잘못된 토큰입니다.");
        }
    }
}
