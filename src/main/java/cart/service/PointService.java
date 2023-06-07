package cart.service;

import cart.domain.Member;
import cart.domain.Point;
import cart.dto.response.PointResponse;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    public PointResponse getUserPoint(final Member member) {
        return new PointResponse(member.getPointValue(), Point.MIN_USAGE_VALUE);
    }
}
