package cart.controller;

import cart.domain.Member;
import cart.exception.AuthTypeException;
import cart.exception.AuthenticationException;
import cart.exception.UnAuthorizedException;
import cart.service.MemberService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTH_TYPE = "basic";
    private final MemberService memberService;

    public MemberArgumentResolver(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final String authorization = extractAuthorization(webRequest);
        final String[] authHeader = extractCredential(authorization);

        final String decodedString = new String(Base64.decodeBase64(authHeader[1]));

        final String[] credentials = decodedString.split(":");
        final String email = credentials[0];
        final String password = credentials[1];

        return resolveMember(email, password);
    }

    private String extractAuthorization(final NativeWebRequest webRequest) {
        final String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        validateAuthorizationEmpty(authorization);
        return authorization;
    }

    private void validateAuthorizationEmpty(final String authorization) {
        if (authorization == null) {
            throw new UnAuthorizedException();
        }
    }

    private String[] extractCredential(final String authorization) {
        String[] authHeader = authorization.split(" ");
        validateAuthType(authHeader);
        return authHeader;
    }

    private void validateAuthType(final String[] authHeader) {
        if (!authHeader[0].equalsIgnoreCase(AUTH_TYPE)) {
            throw new AuthTypeException();
        }
    }

    private Member resolveMember(final String email, final String password) {
        final Member member = memberService.findByEmail(email);
        validateCheckPassword(password, member);
        return member;
    }

    private void validateCheckPassword(final String password, final Member member) {
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }
    }
}
