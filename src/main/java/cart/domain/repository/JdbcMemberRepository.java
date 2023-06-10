package cart.domain.repository;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.member.Member;
import cart.domain.vo.Cash;
import cart.domain.vo.Point;
import cart.exception.AuthenticationException;
import cart.exception.MemberException;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final MemberDao memberDao;

    public JdbcMemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void updatePoint(long memberId, int updatePoint) {
        memberDao.updatePoint(memberId, updatePoint);
    }

    @Override
    public Member getMemberById(long memberId) {
        MemberEntity memberEntity = memberDao.getMemberById(memberId)
                .orElseThrow(MemberException.InvalidIdByNull::new);
        return toMember(memberEntity);
    }

    @Override
    public void updateMoney(long memberId, int money) {
        memberDao.updateMoney(memberId, money);
    }

    @Override
    public Member getMemberByEmail(String email) {
        MemberEntity memberEntity = memberDao.getMemberByEmail(email)
                .orElseThrow(() -> new AuthenticationException("등록되지 않은 사용자 이메일(" + email + ")입니다."));
        return toMember(memberEntity);
    }

    private Member toMember(MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                new Point(memberEntity.getPoint()),
                new Cash(memberEntity.getMoney())
        );
    }
}
