package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long withdrawPoint(final Member member, final Long point) {
        final Member memberEntity = memberDao.getMemberById(member.getId());
        memberEntity.withdraw(point);
        memberDao.updateMember(member);

        return memberEntity.getCash();
    }

    public void depositPoint(final Member member, final Long point) {
        final Member memberEntity = memberDao.getMemberById(member.getId());
        member.deposit(point);
        memberDao.updateMember(member);
    }
}
