package cart.db.repository;

import cart.db.dao.MemberDao;
import cart.db.entity.MemberEntity;
import cart.domain.member.Member;
import cart.domain.member.repository.MemberRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final MemberDao memberDao;

    public JdbcMemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberDao.findByEmail(email)
                .map(MemberEntity::toDomain);
    }

    @Override
    public Optional<Member> findById(long id) {
        return memberDao.findById(id)
                .map(MemberEntity::toDomain);
    }

    @Override
    public Member save(Member member) {
        Long id = memberDao.save(new MemberEntity(member.getEmail(), member.getPassword()));
        return new Member(id, member.getEmail(), member.getPassword());
    }
}
