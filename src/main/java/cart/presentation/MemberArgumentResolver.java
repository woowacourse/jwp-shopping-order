package cart.presentation;


import cart.presentation.dto.request.AuthInfo;
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

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String[] credentials = extractCredentials(authorization);
        String email = credentials[0];
        String password = credentials[1];
        return new AuthInfo(email, password);
    }

    private String[] extractCredentials(String authorization) {
        String[] authHeader = authorization.split(" ");
        String decodedString = new String(Base64.decodeBase64(authHeader[1]));
        return decodedString.split(":");
    }
}
