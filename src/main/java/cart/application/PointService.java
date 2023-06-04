package cart.application;

import cart.dao.MemberRewardPointDao;
import cart.domain.Member;
import cart.domain.point.MemberPoints;
import cart.domain.point.Point;
import cart.dto.PointResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {

    private final MemberRewardPointDao memberRewardPointDao;

    public PointService(MemberRewardPointDao memberRewardPointDao) {
        this.memberRewardPointDao = memberRewardPointDao;
    }

    public PointResponse getPoint(Member member) {
        List<Point> points = memberRewardPointDao.getAllByMemberId(member.getId());
        MemberPoints memberPoints = new MemberPoints(member, points);
        return new PointResponse(memberPoints.getUsablePoints(), memberPoints.getToBeExpiredPoints());
    }
}
