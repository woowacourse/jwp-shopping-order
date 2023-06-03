package cart.member.application;

import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public Long depositCash(final Member member, final Long cash) {
        final Member memberEntity = memberDao.getMemberByEmail(member.getEmail());
        memberEntity.deposit(cash);
        memberDao.updateMember(member);

        return memberEntity.getCash();
    }

    @Transactional
    public void withdrawCash(final Member member, final Long cash) {
        final Member memberEntity = memberDao.getMemberByEmail(member.getEmail());
        memberEntity.withdraw(cash);
        memberDao.updateMember(memberEntity);
    }

    @Transactional(readOnly = true)
    public Long findCash(final Member member) {
        final Member memberEntity = memberDao.getMemberByEmail(member.getEmail());

        return memberEntity.getCash();
    }
}
