package cart.repository;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MemberRepository {
    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findByEmail(final String email) {
        MemberEntity memberEntity = memberDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다"));
        return memberEntity.toMember();
    }

    public Member findById(Long id) {
        MemberEntity memberEntity = memberDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다"));
        return memberEntity.toMember();
    }

    public List<Member> findAll() {
        List<MemberEntity> all = memberDao.findAll();
        return all.stream()
                .map(memberEntity -> memberEntity.toMember())
                .collect(Collectors.toList());
    }
}
