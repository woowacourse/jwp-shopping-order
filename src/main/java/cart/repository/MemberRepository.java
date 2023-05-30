package cart.repository;

import static cart.exception.ErrorMessage.NOT_FOUND_MEMBER;

import cart.domain.Member;
import cart.exception.MemberException;
import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findById(Long id) {
        MemberEntity memberEntity = memberDao.findById(id)
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));

        return toDomain(memberEntity);
    }

    public Member findByEmail(final String email) {
        MemberEntity memberEntity = memberDao.findByEmail(email)
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));

        return toDomain(memberEntity);
    }

    public List<Member> findAll() {
        List<MemberEntity> memberEntities = memberDao.findAll();

        return memberEntities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Member toDomain(MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getPoint()
        );
    }
}
