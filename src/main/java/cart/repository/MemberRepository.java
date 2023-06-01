package cart.repository;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import cart.exception.IllegalMemberException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findById(long id) {
        MemberEntity memberEntity = memberDao.getMemberById(id)
            .orElseThrow(() -> new IllegalMemberException("존재하지 않는 사용자 id 입니다."));

        return memberEntity.toDomain();
    }

    public Member findByEmail(String email) {
        MemberEntity memberEntity = memberDao.getMemberByEmail(email)
            .orElseThrow(() -> new IllegalMemberException("존재하지 않는 이메일입니다."));

        return memberEntity.toDomain();
    }

    public List<Member> findAll() {
        List<MemberEntity> allMembers = memberDao.getAllMembers();
        return allMembers.stream()
            .map(MemberEntity::toDomain)
            .collect(Collectors.toUnmodifiableList());
    }
}
