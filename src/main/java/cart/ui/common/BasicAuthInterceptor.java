package cart.ui.common;

import cart.application.MemberService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

public class BasicAuthInterceptor implements HandlerInterceptor {

    private final BasicAuthenticationExtractor basicAuthenticationExtractor;
    private final MemberService memberService;

    public BasicAuthInterceptor(BasicAuthenticationExtractor basicAuthenticationExtractor, MemberService memberService) {
        this.basicAuthenticationExtractor = basicAuthenticationExtractor;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        MemberAuth memberAuth = basicAuthenticationExtractor.extract(authorization);
        memberService.getMemberByEmailAndPassword(memberAuth.getEmail(), memberAuth.getPassword());
        return true;
    }
}
