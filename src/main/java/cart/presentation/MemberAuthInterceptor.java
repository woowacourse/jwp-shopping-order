package cart.presentation;

import cart.exception.application.AuthenticationException;
import cart.application.service.MemberService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class MemberAuthInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public MemberAuthInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        validate(authorization);
        String[] credentials = extractCredentials(authorization);
        String email = credentials[0];
        String password = credentials[1];
        memberService.validateMemberProfile(email, password);
        return true;
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
