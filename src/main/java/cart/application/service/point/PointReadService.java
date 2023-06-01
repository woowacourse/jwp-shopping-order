package cart.application.service.point;

import cart.application.repository.PointRepository;
import cart.domain.Member;
import cart.domain.Point;
import cart.ui.point.dto.PointResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PointReadService {

    private final PointRepository pointRepository;

    public PointReadService(final PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public PointResponse findPointByMember(final Member member) {
        final Point pointByMember = pointRepository.findPointByMember(member);

        return null;
    }
}
