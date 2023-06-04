package cart.service;

import cart.domain.Member;
import cart.domain.point.Point;
import cart.dto.point.PointResponse;
import cart.repository.PointRepository;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public PointResponse findPointByMember(Member member) {
        Point point = pointRepository.findPointByMemberId(member.getId());
        return new PointResponse(point.getPoint());
    }

}
