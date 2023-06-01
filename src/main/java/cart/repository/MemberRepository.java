package cart.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cart.dao.MemberDao;
import cart.domain.Member;

@Repository
public class MemberRepository {
    private final MemberDao memberDao;

    public MemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> findAll() {
        return memberDao.findAll();
    }

    public Member findByEmail(String email) {
        return memberDao.findByEmail(email);
    }
}
