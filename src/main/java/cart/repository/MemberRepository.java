package cart.repository;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.entity.MemberEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

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
