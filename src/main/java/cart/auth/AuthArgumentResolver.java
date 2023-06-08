package cart.auth;

import cart.auth.dao.AuthDao;
import cart.auth.dto.AuthorizationDto;
import cart.domain.Member;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION = "Authorization";

    private final AuthDao authDao;

    public AuthArgumentResolver(final AuthDao authDao) {
        this.authDao = authDao;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class) &&
                parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        final String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
        final AuthorizationDto authorizationDto = BasicAuthorizationExtractor.extract(authorizationHeader);
        final String email = authorizationDto.getEmail();
        final long memberId = authDao.findIdByEmailAndPassword(email, authorizationDto.getPassword());
        return new Member(memberId, email);
    }
}
