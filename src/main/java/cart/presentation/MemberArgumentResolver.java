package cart.presentation;

import cart.application.service.MemberService;
import cart.application.exception.AuthenticationException;
import cart.presentation.dto.request.AuthInfo;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    public MemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        validate(authorization);
        String[] credentials = extractCredentials(authorization);
        String email = credentials[0];
        String password = credentials[1];
        memberService.validateMemberProfile(email, password);
        return new AuthInfo(email, password);
    }

    private void validate(String authorization) {
        validateNotNull(authorization);
        validateIsBasicEncoded(authorization);
    }

    private void validateNotNull(String authorization) {
        if (Objects.isNull(authorization)) {
            throw new AuthenticationException();
        }
    }

    private void validateIsBasicEncoded(String authorization) {
        String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            throw new AuthenticationException();
        }
    }

    private String[] extractCredentials(String authorization) {
        String[] authHeader = authorization.split(" ");
        String decodedString = new String(Base64.decodeBase64(authHeader[1]));
        return decodedString.split(":");
    }
}
