package cart.repository;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.entity.MemberEntity;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final MemberDao memberDao;

    public JdbcMemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Member findById(final Long id) {
        return memberDao.findById(id)
                .orElseThrow(NoSuchElementException::new)
                .toDomain();
    }

    @Override
    public Member findByEmail(final String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(NoSuchElementException::new)
                .toDomain();
    }

    @Override
    public List<Member> findAll() {
        return memberDao.findAll().stream()
                .map(MemberEntity::toDomain)
                .collect(Collectors.toList());
    }
}
