package cart.application;

import cart.domain.point.PointRepository;
import cart.domain.member.Member;
import cart.domain.point.Point;
import cart.domain.point.PointPolicy;
import cart.domain.time.Region;
import cart.domain.time.TimestampGenerator;
import cart.dto.member.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class MemberService {

    private final PointRepository pointRepository;
    private final PointPolicy pointPolicy;

    public MemberService(PointRepository pointRepository, PointPolicy pointPolicy) {
        this.pointRepository = pointRepository;
        this.pointPolicy = pointPolicy;
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo(Member member) {
        Timestamp createdAt = TimestampGenerator.getCurrentTime(Region.KOREA);

        long point = pointRepository.findAllAvailablePointsByMemberId(member.getId(), createdAt)
                .stream()
                .mapToLong(Point::getLeftPoint)
                .sum();
        return new MemberResponse(member.getEmail(), point, pointPolicy.getEarningRate(member));
    }
}
