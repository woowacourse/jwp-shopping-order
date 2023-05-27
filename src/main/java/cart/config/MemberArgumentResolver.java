package cart.config;

import java.util.List;

import cart.dto.AuthMember;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String MEMBER_INFO_DELIMITER = ":";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authValue = webRequest.getHeader(AUTH_HEADER_KEY);
        String decodedMemberInfo = BasicAuthDecoder.decode(authValue);

        List<String> memberInfo = List.of(decodedMemberInfo.split(MEMBER_INFO_DELIMITER));
        String email = memberInfo.get(0);
        String password = memberInfo.get(1);
        return new AuthMember(email, password);
    }
}
