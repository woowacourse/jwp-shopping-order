package cart.repository;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.entity.MemberEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Optional<Member> findByEmail(String email) {
        return memberDao.findByEmail(email)
                .map(MemberEntity::toDomain);
    }

    public Optional<Member> findById(long id) {
        return memberDao.findById(id)
                .map(MemberEntity::toDomain);
    }

    public Member save(Member member) {
        Long id = memberDao.save(new MemberEntity(member.getEmail(), member.getPassword()));
        return new Member(id, member.getEmail(), member.getPassword());
    }
}
