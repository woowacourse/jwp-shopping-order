package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.Point;
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
        member.decreasePoint(point);
        memberDao.updateMember(member);
    }

    @Transactional
    public void increasePoint(Member member, Point point) {
        member.increasePoint(point);
        memberDao.updateMember(member);
    }
}
