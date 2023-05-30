package cart.application;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.exception.notfound.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberPoint getPoint(final Member member) {
        final Member findMember = memberDao.findByEmail(member.getEmailValue())
                .orElseThrow(() -> new MemberNotFoundException(member.getEmailValue()));
        return findMember.getPoint();
    }
}
