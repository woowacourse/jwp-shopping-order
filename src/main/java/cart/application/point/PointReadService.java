package cart.application.point;

import cart.domain.Point;
import cart.domain.repository.point.PointRepository;
import cart.ui.MemberAuth;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PointReadService {

    private final PointRepository pointRepository;

    public PointReadService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public PointResultDto findPointByMember(MemberAuth memberAuth) {
        final Point pointByMember = pointRepository.findPointByMemberId(memberAuth.getId());
        return PointResultDto.of(pointByMember.getPointHistories(), pointByMember.calculateTotalPoint());
    }

}
