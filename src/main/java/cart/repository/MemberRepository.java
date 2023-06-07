package cart.repository;

import cart.dao.MemberDao;
import cart.domain.AuthMember;
import cart.domain.Member;
import cart.entity.AuthMemberEntity;
import cart.entity.MemberEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public AuthMember save(AuthMember member) {
        Long id = memberDao.save(
                new AuthMemberEntity(
                        new MemberEntity(member.getEmail()),
                        member.getPassword())
        );
        return new AuthMember(new Member(id, member.getEmail()), member.getPassword());
    }

    public Optional<AuthMember> findAuthMemberById(long id) {
        return memberDao.findAuthMemberById(id)
                .map(AuthMemberEntity::toDomain);
    }

    public Optional<AuthMember> findAuthMemberByEmail(String email) {
        return memberDao.findAuthMemberByEmail(email)
                .map(AuthMemberEntity::toDomain);
    }
}
