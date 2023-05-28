package cart.repository;

import static java.util.stream.Collectors.toList;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> findAll() {
        return memberDao.findAll().stream()
                .map(this::toMember)
                .collect(toList());
    }

    private Member toMember(final MemberEntity entity) {
        return new Member(entity.getId(), entity.getEmail(), entity.getPassword());
    }

    public Optional<Member> findById(final Long memberId) {
        return memberDao.findById(memberId).map(this::toMember);
    }

    public Member save(final Member member) {
        final MemberEntity memberEntity = memberDao.insert(MemberEntity.from(member));
        return toMember(memberEntity);
    }
}
