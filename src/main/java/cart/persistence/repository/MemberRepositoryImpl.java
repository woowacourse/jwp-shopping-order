package cart.persistence.repository;

import cart.application.repository.MemberRepository;
import cart.persistence.dao.MemberDao;
import cart.application.domain.Member;
import cart.persistence.entity.MemberEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberDao memberDao;

    public MemberRepositoryImpl(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public List<Member> findAll() {
        return memberDao.findAll().stream()
                .map(this::toMember)
                .collect(Collectors.toList());
    }

    private Member toMember(final MemberEntity memberEntity) {
        return new Member(memberEntity.getId(), memberEntity.getGrade(), memberEntity.getEmail(),
                memberEntity.getPassword());
    }

}
