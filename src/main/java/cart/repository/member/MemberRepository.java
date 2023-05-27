package cart.repository.member;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.entity.MemberEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findByEmail(final String email) {
        MemberEntity memberEntity = memberDao.getMemberByEmail(email);
        return new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword());
    }

    public List<Member> findAll() {
        return memberDao.getAllMemberEntities().stream()
                .map(entity -> new Member(entity.getId(), entity.getEmail(), entity.getPassword()))
                .collect(Collectors.toList());
    }

    public Member findMemberById(final long id) {
        MemberEntity memberEntity = memberDao.getMemberById(id);
        return new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword());
    }
}
