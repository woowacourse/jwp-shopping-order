package cart.application;

import cart.domain.Member;
import cart.domain.Point;
import cart.domain.repository.PointRepository;
import cart.dto.PointResponse;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final PointRepository pointRepository;

    public PointService(final PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public PointResponse findPointByMember(Member member){
        final Point pointByMemberId = pointRepository.findPointByMemberId(member.getId());
        return new PointResponse(pointByMemberId.getPoint().longValue());
    }
}
