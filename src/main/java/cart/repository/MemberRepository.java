package cart.repository;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import cart.exception.MemberException.NotFound;
import cart.repository.mapper.MemberMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;
    private final MemberMapper memberMapper;

    public MemberRepository(MemberDao memberDao, MemberMapper memberMapper) {
        this.memberDao = memberDao;
        this.memberMapper = memberMapper;
    }

    public List<Member> getAllMembers() {
        List<MemberEntity> memberEntities = memberDao.getAllMembers();
        return memberEntities.stream()
                .map(memberMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Member getMemberByEmailAndPassword(String email, String password) {
        MemberEntity memberEntity = memberDao.getMemberByEmailAndPassword(email, password)
                .orElseThrow(() -> new NotFound());
        return memberMapper.toDomain(memberEntity);
    }
}
