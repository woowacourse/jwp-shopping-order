package cart.member.repository;

import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    private final MemberDao memberDao;
    
    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }
    
    public Member getMemberByEmail(final String memberEmail) {
        final MemberEntity memberEntity = memberDao.getMemberByEmail(memberEmail);
        return Member.from(memberEntity);
    }
    
    public Member getMemberById(final long id) {
        return Member.from(memberDao.getMemberById(id));
    }
    
    public void update(final Member memberByEmail) {
        final MemberEntity memberEntity = new MemberEntity(
                memberByEmail.getId(),
                memberByEmail.getEmail(),
                memberByEmail.getPassword(),
                memberByEmail.getPoint()
        );
        memberDao.update(memberEntity);
    }
}
