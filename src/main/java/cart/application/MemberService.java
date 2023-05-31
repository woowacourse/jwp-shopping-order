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

    public Long depositCash(final Member member, final Long cash) {
        final Member memberEntity = memberDao.getMemberByEmail(member.getEmail());
        memberEntity.deposit(cash);
        memberDao.updateMember(member);

        return memberEntity.getCash();
    }

    public void withdrawCash(final Member member, final Long cash) {
        final Member memberEntity = memberDao.getMemberByEmail(member.getEmail());
        memberEntity.withdraw(cash);
        memberDao.updateMember(memberEntity);
    }

    public Long findCash(final Member member) {
        final Member memberEntity = memberDao.getMemberByEmail(member.getEmail());

        return memberEntity.getCash();
    }
}
