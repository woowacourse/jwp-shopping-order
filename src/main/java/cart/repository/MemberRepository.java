package cart.repository;

import cart.domain.Member;
import cart.db.dao.MemberDao;
import cart.db.entity.MemberEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member getMemberById(Long id) {
        return memberDao.getMemberById(id).toDomain();
    }

    public Member getMemberByEmail(String email) {
        return memberDao.getMemberByEmail(email).toDomain();
    }

    public void addMember(Member member) {
        memberDao.addMember(MemberEntity.of(member));
    }

    public void updateMember(Member member) {
        memberDao.updateMember(MemberEntity.of(member));
    }

    public void deleteMember(Long id) {
        memberDao.deleteMember(id);
    }

    public List<Member> getAllMembers() {
        return memberDao.getAllMembers().stream()
                .map(MemberEntity::toDomain)
                .collect(Collectors.toList());
    }
}

