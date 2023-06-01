package cart.repository;

import cart.dao.MemberDao;
import cart.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    private final MemberDao memberDao;
    
    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }
    
    public Member getMemberByEmail(final String memberEmail) {
        return memberDao.getMemberByEmail(memberEmail);
    }
    
    public Member getMemberById(final long id) {
        return Member.from(memberDao.getMemberById(id));
    }
}
