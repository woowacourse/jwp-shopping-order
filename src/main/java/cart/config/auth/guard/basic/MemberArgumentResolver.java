package cart.config.auth.guard.basic;

import cart.domain.member.Member;
import cart.exception.AuthenticationException;
import cart.repository.member.MemberRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BASIC_HEADER = "basic";
    private static final String AUTHORIZATION_SEPARATOR = " ";
    private static final String CREDENTIAL_SEPARATOR = ":";

    private final MemberRepository memberRepository;

    public MemberArgumentResolver(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        validateNullAuthorization(authorization);

        String[] authHeader = authorization.split(AUTHORIZATION_SEPARATOR);

        validateHeader(authHeader);

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);

        String[] credentials = decodedString.split(CREDENTIAL_SEPARATOR);
        String email = credentials[0];
        String password = credentials[1];
        Member member = memberRepository.findByEmail(email);

        validateMember(password, member);
        return member;
    }

    private void validateNullAuthorization(final String authorization) {
        if (authorization == null) {
            throw new AuthenticationException();
        }
    }

    private void validateHeader(final String[] authHeader) {
        if (!authHeader[0].equalsIgnoreCase(BASIC_HEADER)) {
            throw new AuthenticationException();
        }
    }

    private void validateMember(final String password, final Member member) {
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }
    }
}
