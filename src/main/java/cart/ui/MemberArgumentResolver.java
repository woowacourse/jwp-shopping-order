package cart.ui;

import cart.application.service.member.MemberReadService;
import cart.application.service.member.dto.MemberResultDto;
import cart.exception.AuthenticationException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(MemberArgumentResolver.class);
    private final MemberReadService memberReadService;

    public MemberArgumentResolver(MemberReadService memberReadService) {
        this.memberReadService = memberReadService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberAuth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null) {
            logger.warn("Not include Authorization Header, request: {}", webRequest.getNativeRequest());
            throw new AuthenticationException();
        }

        String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            logger.warn("Non-Basic Authorization Header, Now Request Header is: {}", authHeader[0]);
            throw new AuthenticationException();
        }

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);

        String[] credentials = decodedString.split(":");
        String email = credentials[0];
        String password = credentials[1];
        final MemberResultDto memberResultDto = memberReadService.findMemberByEmail(email);
        // 본인 여부 확인
        if (!memberResultDto.getPassword().equals(password)) {
            logger.warn("Invalid Password, Input Email: {}", email);
            throw new AuthenticationException();
        }

        return new MemberAuth(memberResultDto.getId(), memberResultDto.getName(), email, password);
    }

}
