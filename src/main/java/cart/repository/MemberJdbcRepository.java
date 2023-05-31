package cart.repository;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.entity.MemberEntity;
import org.springframework.stereotype.Repository;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private final MemberDao memberDao;

    public MemberJdbcRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Member findByEmail(final String email) {
        final MemberEntity memberEntity = memberDao.findByEmail(email);
        return toDomain(memberEntity);
    }

    @Override
    public Member findById(final long id) {
        final MemberEntity memberEntity = memberDao.findById(id);
        return toDomain(memberEntity);
    }

    private Member toDomain(final MemberEntity entity) {
        return new Member(entity.getId(), entity.getEmail());
    }
}
