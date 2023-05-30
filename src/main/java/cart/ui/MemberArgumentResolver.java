package cart.ui;

import cart.error.exception.AuthenticationException;
import cart.persistence.member.MemberJdbcRepository;
import cart.domain.member.Member;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberJdbcRepository memberJdbcRepository;

    public MemberArgumentResolver(MemberJdbcRepository memberJdbcRepository) {
        this.memberJdbcRepository = memberJdbcRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            return null;
        }

        String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            return null;
        }

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);

        String[] credentials = decodedString.split(":");
        String email = credentials[0];
        String password = credentials[1];

        // 본인 여부 확인
        Member member = memberJdbcRepository.findByEmail(email)
            .orElseThrow(()-> new IllegalArgumentException("해당 이메일을 가진 유저가 없습니다."));
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }
        return member;
    }
}
