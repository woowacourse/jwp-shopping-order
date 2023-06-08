package cart.ui;

import cart.domain.Member;
import cart.domain.repository.MemberRepository;
import cart.exception.AuthenticationException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private static final int DEFAULT_TOKEN_LENGTH = 2;
    private final MemberRepository memberRepository;

    public MemberArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            throw new AuthenticationException("현재 입력된 사용자 정보가 없습니다.");
        }

        String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            throw new AuthenticationException("basic 인증 관련 문제가 발생했습니다. Authorization : " + authorization);
        }

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);

        String[] credentials = decodedString.split(":");
        if (credentials.length != DEFAULT_TOKEN_LENGTH) {
            throw new AuthenticationException("basic 인증 관련 문제가 발생했습니다.");
        }

        String email = credentials[0];
        String password = credentials[1];

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("올바르지 않은 계정 정보입니다 email : " + email));

        if (!member.checkPassword(password)) {
            throw new AuthenticationException("올바르지 않은 계정 정보입니다");
        }
        return member;
    }
}
