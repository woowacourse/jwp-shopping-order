package cart.persistence;

import cart.application.mapper.MemberMapper;
import cart.application.repository.MemberRepository;
import cart.domain.Member;
import cart.persistence.dao.MemberDao;
import cart.persistence.entity.MemberEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private final MemberDao memberDao;

    public MemberJdbcRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        Optional<MemberEntity> optionalMemberEntity = memberDao.findMemberByEmail(email);
        if (optionalMemberEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(MemberMapper.toMember(optionalMemberEntity.get()));
    }
}
