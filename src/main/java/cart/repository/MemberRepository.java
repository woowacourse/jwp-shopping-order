package cart.repository;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import cart.exception.MemberException.NotFound;
import cart.repository.mapper.MemberMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;
    private final MemberMapper memberMapper;

    public MemberRepository(MemberDao memberDao, MemberMapper memberMapper) {
        this.memberDao = memberDao;
        this.memberMapper = memberMapper;
    }

    public Member findByEmailAndPassword(String email, String password) {
        MemberEntity memberEntity = memberDao.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new NotFound());
        return memberMapper.toDomain(memberEntity);
    }
}
