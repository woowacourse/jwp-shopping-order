package cart.repository;

import cart.dao.MemberDao;
import cart.dao.MemberEntity;
import cart.domain.member.Email;
import cart.domain.member.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> getAllMembers() {
        final List<MemberEntity> memberEntities = memberDao.getAllMembers();
        return memberEntities.stream()
                .map(memberEntity -> new Member(
                        memberEntity.getId(),
                        memberEntity.getEmail(),
                        memberEntity.getPassword()
                ))
                .collect(Collectors.toUnmodifiableList());
    }

    public Member getMemberByEmail(final Email email) {
        final MemberEntity memberEntity = memberDao.getMemberByEmail(email.getAddress());
        return new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword());
    }
}
