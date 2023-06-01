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
        final Optional<MemberEntity> member = memberDao.getMemberByEmail(email);
        if (member.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Member.from(member.get()));
    }

    public Optional<Member> findById(final Long id) {
        final Optional<MemberEntity> member = memberDao.getMemberById(id);
        if (member.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Member.from(member.get()));
    }
}
