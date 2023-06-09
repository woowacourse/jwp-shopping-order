package cart.auth;

import cart.domain.Member;
import cart.domain.value.Email;
import cart.exception.AuthenticationException;
import cart.repository.MemberRepository;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;
    private final BasicAuthorizationExtractor basicAuthorizationExtractor;

    public AuthArgumentResolver(
            final MemberRepository memberRepository,
            final BasicAuthorizationExtractor basicAuthorizationExtractor
    ) {
        this.memberRepository = memberRepository;
        this.basicAuthorizationExtractor = basicAuthorizationExtractor;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        String header = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        AuthInfo authInfo = basicAuthorizationExtractor.extract(header);

        Member member = memberRepository.findByEmail(new Email(authInfo.getEmail()));
        if (!member.isSamePassword(authInfo.getPassword())) {
            throw new AuthenticationException();
        }
        return member;
    }
}
