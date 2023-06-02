package cart.repository;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member getMemberById(Long id) {
        return memberDao.getMemberById(id);
    }

    public Member getMemberByEmail(String email) {
        return memberDao.getMemberByEmail(email);
    }

    public List<Member> getAllMember() {
        return memberDao.getAllMembers();
    }
}
