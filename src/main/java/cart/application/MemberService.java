package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.MemberResponse;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void update(final Member member, final int money) {
        member.update(money);
        memberDao.updateMember(member);
    }

    public MemberResponse findByEmail(final Member member) {
        Member findedMember = memberDao.getMemberByEmail(member.getEmail());
        return new MemberResponse(findedMember);
    }
}
