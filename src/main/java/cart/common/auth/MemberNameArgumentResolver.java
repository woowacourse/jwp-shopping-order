package cart.common.auth;

import cart.application.MemberService;
import cart.application.dto.MemberResponse;
import cart.exception.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MemberNameArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String DELIMITER = ":";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final MemberService memberService;

    public MemberNameArgumentResolver(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MemberName.class) &&
            parameter.getParameterType().isAssignableFrom(String.class);
    }

    @Override
    public String resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) {
        final String authorization = webRequest.getHeader(AUTHORIZATION_HEADER);
        final String memberToken = BasicTokenProvider.extractToken(authorization);
        final String memberName = memberToken.split(DELIMITER)[0];
        final String memberPassword = memberToken.split(DELIMITER)[1];

        final MemberResponse memberResponse = memberService.getByName(memberName);
        if (!memberResponse.getPassword().equals(memberPassword)) {
            throw new AuthenticationException();
        }
        return memberName;
    }
}
