package cart.repository;

import cart.dao.MemberDao;
import cart.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Optional<Member> findById(Long id) {
        return memberDao.findById(id);
    }

    public Optional<Member> findByEmail(String email) {
        return memberDao.findByEmail(email);
    }

    public List<Member> findAll() {
        return memberDao.findAll();
    }

    public void update(Member member) {
        memberDao.update(member);
    }
}
