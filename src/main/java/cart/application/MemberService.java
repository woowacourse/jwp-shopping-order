package cart.application;

import cart.dao.point.PointDao;
import cart.domain.member.Member;
import cart.domain.point.Point;
import cart.domain.point.PointPolicy;
import cart.domain.time.Region;
import cart.domain.time.TimestampGenerator;
import cart.dto.member.MemberResponse;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class MemberService {

    private final PointDao pointDao;
    private final PointPolicy pointPolicy;

    public MemberService(PointDao pointDao, PointPolicy pointPolicy) {
        this.pointDao = pointDao;
        this.pointPolicy = pointPolicy;
    }

    public MemberResponse getMemberInfo(Member member) {
        Timestamp createdAt = TimestampGenerator.getCurrentTime(Region.KOREA);

        long point = pointDao.findAllAvailablePointsByMemberId(member.getId(), createdAt)
                .stream()
                .mapToLong(Point::getLeftPoint)
                .sum();
        return new MemberResponse(member.getEmail(), point, pointPolicy.getEarningRate(member));
    }
}
