package cart.application;

import cart.domain.Member;
import cart.domain.PointExpirePolicy;
import cart.domain.Points;
import cart.dto.PointResponse;
import cart.repository.PointRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class PointService {

    private final PointRepository pointRepository;
    private final PointExpirePolicy pointExpirePolicy;

    public PointService(PointRepository pointRepository, PointExpirePolicy pointExpirePolicy) {
        this.pointRepository = pointRepository;
        this.pointExpirePolicy = pointExpirePolicy;
    }

    public PointResponse findBy(Member member) {
        Points currentPoints = pointRepository.findUsablePointsByMemberId(member.getId());
        Points toBeExpiredPoints = new Points(currentPoints.getPoints().stream()
                .filter(point -> pointExpirePolicy.isSoonExpireDate(LocalDate.now(), point.getExpiredAt()))
                .collect(Collectors.toList()));
        return new PointResponse(currentPoints.getTotalPoint(), toBeExpiredPoints.getTotalPoint());
    }
}
