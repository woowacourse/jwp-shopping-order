package cart.repository;

import cart.dao.MemberDao2;
import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepository {
    private MemberDao2 memberDao;

    public MemberRepository(final MemberDao2 memberDao) {
        this.memberDao = memberDao;
    }

    public Member findByEmail(final String email) {
        MemberEntity memberEntity = memberDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다"));
        return memberEntity.toMember();
    }
}
