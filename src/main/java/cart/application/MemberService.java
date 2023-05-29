package cart.application;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member getPoint(final Member member) {
        final Optional<Member> memberOptional = memberDao.findByEmail(member.getEmailValue());
        return memberOptional.orElseThrow(() -> new MemberNotFoundException(member.getEmailValue()));
    }
}
