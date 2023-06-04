package cart.application;

import cart.domain.Member;
import cart.dto.point.PointResponse;
import cart.repository.OrderRepository;
import cart.repository.PointRepository;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public PointResponse findPointByMember(Member member) {
        Long point = pointRepository.findPointByMemberId(member.getId());
        return new PointResponse(point);
    }

}
