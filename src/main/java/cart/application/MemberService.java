package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.exception.AuthenticationException;
import cart.exception.MemberNotExistException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
        final Member member = memberDao.findMemberByEmail(email)
                .orElseThrow(() -> new MemberNotExistException(email));
        if (member.checkPassword(password)) {
            return member;
        }

        throw new AuthenticationException("회원의 비밀번호가 일치하지 않습니다.");
    }

    public Member findMemberById(final long memberId) {
        return memberDao.findMemberById(memberId)
                .orElseThrow(() -> new MemberNotExistException(memberId));
    }
}
