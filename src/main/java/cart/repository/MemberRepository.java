package cart.repository;

import cart.repository.dao.MemberDao;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void updatePoint(long memberId, int updatePoint) {
        memberDao.updatePoint(memberId, updatePoint);
    }
}
