package cart.auth;

import cart.domain.member.Member;
import cart.exception.AuthenticationException;
import cart.service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final AuthContext authContext;

    public AuthInterceptor(final MemberService memberService, final AuthContext authContext) {
        this.memberService = memberService;
        this.authContext = authContext;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        final AuthInfo authInfo = BasicAuthExtractor.extract(header);
        final Member member = memberService.getMemberByEmail(authInfo.getEmail());

        if (member.hasSamePassword(authInfo.getPassword())) {
            authContext.set(member);
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
        throw new AuthenticationException("입력한 정보의 회원이 존재하지 않습니다.");
    }
}
