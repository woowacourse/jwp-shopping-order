package cart.application.service.point;

import cart.application.repository.point.PointRepository;
import cart.application.service.point.dto.PointResultDto;
import cart.domain.member.Member;
import cart.domain.point.PointHistories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PointReadService {

    private final PointRepository pointRepository;

    public PointReadService(final PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public PointResultDto findPointByMember(final Member member) {
        final PointHistories pointByMember = pointRepository.findPointByMemberId(member.getId());
        return PointResultDto.of(pointByMember.getPointHistories(), pointByMember.calculateTotalPoint());
    }

}
