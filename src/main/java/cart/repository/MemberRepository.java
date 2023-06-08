package cart.repository;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Optional<Member> findByEmail(final String email) {
        return memberDao.findByEmail(email)
                .map(MemberEntity::create);
    }

    public Optional<Member> findById(final Long id) {
        return memberDao.findById(id)
                .map(MemberEntity::create);
    }
}
