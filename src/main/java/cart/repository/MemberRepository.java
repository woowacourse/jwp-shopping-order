package cart.repository;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.vo.Money;
import cart.entity.MemberEntity;
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
                .orElseThrow(MemberException.NotExist::new)
                .toDomain();
    }

    public Member findByMemberEmail(String email) {
        return memberDao.getByMemberEmail(email)
                .orElseThrow(MemberException.NotExist::new)
                .toDomain();
    }

    public void updateMember(MemberEntity memberEntity) {
        memberDao.updateMember(null, memberEntity);
    }

    public void updateMinusMoney(Long memberId, Money minusMoney) {
        memberDao.updateMinusMoney(memberId, minusMoney);
    }
}
