package cart.repository;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.entity.MemberEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MysqlMemberRepository implements MemberRepository {

    private final MemberDao memberDao;

    public MysqlMemberRepository(final MemberDao memberDao) {
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
