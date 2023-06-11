package cart.ui.api;

import cart.domain.member.Member;
import cart.domain.repository.JdbcMemberRepository;
import cart.exception.MemberException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JdbcMemberRepository memberRepository;

    public MemberArgumentResolver(JdbcMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        String[] authHeader = authorization.split(" ");
        String decodedString = new String(Base64.decodeBase64(authHeader[1]));

        String[] credentials = decodedString.split(":");
        String email = credentials[0];

        return memberRepository.getMemberByEmail(email)
                .orElseThrow(MemberException.InvalidEmail::new);
    }
}
