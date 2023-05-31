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

    public Long depositPoint(final Member member, final Long point) {
        final Member memberEntity = memberDao.getMemberByEmail(member.getEmail());
        memberEntity.deposit(point);
        memberDao.updateMember(member);

        return memberEntity.getCash();
    }

    public void withdrawPoint(final Member member, final Long point) {
        final Member memberEntity = memberDao.getMemberByEmail(member.getEmail());
        memberEntity.withdraw(point);
        memberDao.updateMember(memberEntity);
    }
}
