package cart.repository;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.member.Member;
import cart.exception.MemberException;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void saveMember(MemberEntity memberEntity) {
        memberDao.insertMember(memberEntity);
    }

    public Member findByMemberId(Long memberId) {
        return memberDao.getByMemberId(memberId)
                .orElseThrow(MemberException.NotFound::new)
                .toDomain();
    }

    public Member findByMemberEmail(String email) {
        return memberDao.getByMemberEmail(email)
                .orElseThrow(MemberException.NotFound::new)
                .toDomain();
    }

    public void updateMember(MemberEntity memberEntity) {
        memberDao.updateMember(memberEntity);
    }
}
