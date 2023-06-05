package cart.authentication;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import cart.domain.Member;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final BasicAuthorizationExtractor extractor;
    private final AuthenticationMemberConverter authConverter;
    private final MemberStore memberStore;

    public AuthenticationInterceptor(
            BasicAuthorizationExtractor extractor,
            AuthenticationMemberConverter memberConverter,
            MemberStore memberStore
    ) {
        this.extractor = extractor;
        this.authConverter = memberConverter;
        this.memberStore = memberStore;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String header = request.getHeader(AUTHORIZATION);
        AuthInfo authInfo = extractor.extract(header);

        Member loginMember = authConverter.convert(authInfo);
        memberStore.set(loginMember);

        return true;
    }
}
