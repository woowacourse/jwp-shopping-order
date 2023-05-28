package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.exception.AuthenticationException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> findAllMembers() {
        return memberDao.findAllMembers();
    }

    public Member findAuthorizedMember(final String email, final String password) {
        final Member member = memberDao.findMemberByEmail(email);
        if (member.checkPassword(password)) {
            return member;
        }

        throw new AuthenticationException();
    }

    public Member findMemberById(final long memberId) {
        return memberDao.findMemberById(memberId);
    }
}
