package cart.config.auth.guard.basic;

import cart.domain.member.Member;
import cart.exception.AuthenticationException;
import cart.repository.member.MemberRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    public MemberArgumentResolver(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
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
        Member member = memberRepository.findByEmail(email);
        if (!member.checkPassword(password)) {
            System.out.println("member : " + member.getEmail() + " " + member.getPassword());
            System.out.println("password: " + password);
            System.out.println("go " + member.checkPassword(password));
            System.out.println("에러!!");
            throw new AuthenticationException();
        }
        return member;
    }
}
