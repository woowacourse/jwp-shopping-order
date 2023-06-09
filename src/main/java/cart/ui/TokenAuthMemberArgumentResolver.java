package cart.ui;

import cart.domain.Member;
import cart.domain.respository.member.MemberRepository;
import cart.exception.InvalidTokenException;
import cart.exception.MemberNotExistException;
import cart.infrastructure.JwtTokenProvider;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class TokenAuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public TokenAuthMemberArgumentResolver(final JwtTokenProvider jwtTokenProvider,
        final MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {

        final String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authorization)) {
            return null;
        }
        final String[] authHeader = authorization.split(" ", -1);
        if (!authHeader[0].equalsIgnoreCase("bearer") || authHeader.length != 2) {
            return null;
        }

        final String token = authHeader[1];
        if (!jwtTokenProvider.validateAccessToken(token)) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }
        final String email = jwtTokenProvider.getClaims(token);
        
        return memberRepository.getMemberByEmail(email)
            .orElseThrow(() -> new MemberNotExistException("멤버가 존재하지 않습니다."));
    }
}
