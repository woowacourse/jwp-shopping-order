package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.Point;
import cart.exception.PointNotEnoughException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {
    private final MemberDao memberDao;

    public PointService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public void decreasePoint(Member member, Point point) {
        validatePoint(member, point);
        member.decreasePoint(point);
        memberDao.updateMember(member);
    }

    private void validatePoint(Member member, Point point) {
        if (member.hasNotEnoughPoint(point)) {
            throw new PointNotEnoughException("포인트가 부족합니다.");
        }
    }

    @Transactional
    public void increasePoint(Member member, Point point) {
        member.increasePoint(point);
        memberDao.updateMember(member);
    }
}
