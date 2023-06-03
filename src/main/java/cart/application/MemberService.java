package cart.application;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.auth.BasicAuthorizationDecoder;
import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.response.MemberResponse;
import cart.exception.BadRequestException;
import cart.exception.ExceptionType;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void logIn(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        Map.Entry<String, String> emailAndPassword = BasicAuthorizationDecoder.decode(authorization);

        Member memberByEmail = memberDao.getMemberByEmail(emailAndPassword.getKey());
        if (!memberByEmail.checkPassword(emailAndPassword.getValue())) {
            throw new BadRequestException(ExceptionType.LOGIN_FAIL);
        }
    }

    public MemberResponse findProfile(Member member) {
        return new MemberResponse(member);
    }
}
