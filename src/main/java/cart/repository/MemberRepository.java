package cart.repository;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import cart.exception.AuthenticationException;
import cart.repository.mapper.MemberMapper;
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
            .orElseThrow(AuthenticationException::new);

        return MemberMapper.toMember(memberEntity);
    }

    public Member findByEmail(String email) {
        MemberEntity memberEntity = memberDao.getMemberByEmail(email)
            .orElseThrow(AuthenticationException::new);

        return MemberMapper.toMember(memberEntity);
    }

    public List<Member> findAll() {
        List<MemberEntity> allMembers = memberDao.getAllMembers();
        return allMembers.stream()
            .map(MemberMapper::toMember)
            .collect(Collectors.toUnmodifiableList());
    }
}
