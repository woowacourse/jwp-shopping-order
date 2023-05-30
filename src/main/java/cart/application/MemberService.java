package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.exception.AuthenticationException;
import cart.infrastructure.AuthInfo;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member checkLoginMember(final AuthInfo authInfo) {
        final Member member = memberDao.getMemberByEmail(authInfo.getEmail());
        if (!member.checkPassword(authInfo.getPassword())) {
            throw new AuthenticationException();
        }
        return member;
    }
}
