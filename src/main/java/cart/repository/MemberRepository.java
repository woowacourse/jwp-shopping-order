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

    public Optional<Member> getMemberByEmail(String email) {
        return memberDao.getMemberByEmail(email)
                .map(MemberEntity::toDomain);
    }

    public Optional<Member> getMemberById(long id) {
        return memberDao.getMemberById(id)
                .map(MemberEntity::toDomain);
    }
}
