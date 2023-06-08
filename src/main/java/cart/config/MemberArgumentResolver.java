package cart.config;

import cart.application.repository.MemberRepository;
import cart.domain.member.Member;
import cart.exception.AuthenticationException;
import cart.ui.MemberAuth;
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
    private final MemberRepository memberRepository;

    public MemberArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(() -> new AuthenticationException());
        // 본인 여부 확인
        if (!member.getPassword().equals(password)) {
            logger.warn("Invalid Password, Input Email: {}", email);
            throw new AuthenticationException();
        }

        return new MemberAuth(member.getId(), member.getName(), email, password);
    }

}
