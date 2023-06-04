package cart.presentation;

import cart.exception.AuthenticationException;
import cart.domain.Member;
import cart.repository.MemberRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int AUTH_TYPE_INDEX = 0;
    private static final int AUTH_VALUE_INDEX = 1;
    private static final int EMAIL_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;
    private final MemberRepository memberRepository;

    public MemberArgumentResolver(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        validateNull(authorization);

        String[] authHeader = authorization.split(" ");
        validateAuthType(authHeader[AUTH_TYPE_INDEX]);

        String[] credentials = encode(authHeader[AUTH_VALUE_INDEX]);
        
        Member member = memberRepository.findByEmail(credentials[EMAIL_INDEX]);
        validatePassword(member, credentials[PASSWORD_INDEX]);

        return member;
    }

    private void validateNull(final String authorization) {
        if (authorization == null) {
            throw new AuthenticationException();
        }
    }

    private void validateAuthType(final String authType) {
        if (!authType.equalsIgnoreCase("basic")) {
            throw new AuthenticationException();
        }
    }

    private String[] encode(final String authValue) {
        byte[] decodedBytes = Base64.decodeBase64(authValue);
        String decodedString = new String(decodedBytes);

        return decodedString.split(":");
    }

    private void validatePassword(final Member member, final String password) {
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }
    }
}
