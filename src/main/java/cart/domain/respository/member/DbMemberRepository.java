package cart.domain.respository.member;

import cart.dao.MemberDao;
import cart.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DbMemberRepository implements MemberRepository {

    private final MemberDao memberDao;

    public DbMemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Optional<Member> getMemberById(final Long id) {
        return memberDao.getMemberById(id);
    }

    @Override
    public Optional<Member> getMemberByEmail(final String email) {
        return memberDao.getMemberByEmail(email);
    }

    @Override
    public void addMember(final Member member) {
        memberDao.addMember(member);
    }

    @Override
    public void updateMember(final Member member) {
        memberDao.updateMember(member);
    }

    @Override
    public void deleteMember(final Long id) {
        memberDao.deleteMember(id);
    }

    @Override
    public List<Member> getAllMembers() {
        return memberDao.getAllMembers();
    }
}
