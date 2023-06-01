package cart.ui.common;

import cart.application.MemberService;
import cart.exception.badrequest.member.MemberPasswordException;
import cart.ui.controller.dto.response.MemberResponse;
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
        MemberResponse member = memberService.getMemberByEmail(memberAuth.getEmail());
        if (!member.getPassword().equals(memberAuth.getPassword())) {
            throw new MemberPasswordException("멤버의 비밀번호가 일치하지 않습니다.");
        }
        return true;
    }
}
