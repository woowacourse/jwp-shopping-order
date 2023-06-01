package cart.repository;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.MemberEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class MemberRepository {

    private final MemberDao memberDao;

    public long save(final Member member) {
        MemberEntity memberEntity = new MemberEntity(member.getEmail(), member.getPassword(), member.getPointValue());

        return memberDao.insert(memberEntity);
    }

    public Member findById(final Long memberId) {
        MemberEntity memberEntity = memberDao.findById(memberId);

        return toDomain(memberEntity);
    }

    public Member findByEmail(final String email) {
        MemberEntity memberEntity = memberDao.findByEmail(email);

        return toDomain(memberEntity);
    }

    public List<Member> findAll() {
        List<MemberEntity> memberEntities = memberDao.findAll();

        return memberEntities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Member toDomain(final MemberEntity memberEntity) {
        return new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword(), memberEntity.getPoint());
    }

    public void update(final Member member) {
        MemberEntity memberEntity = new MemberEntity(member.getId(), member.getEmail(), member.getPassword(), member.getPointValue());

        memberDao.update(memberEntity);
    }

    public void delete(final Member member) {
        memberDao.delete(member.getId());
    }
}
