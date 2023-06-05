package cart.application;

import cart.domain.Member;
import cart.domain.Points;
import cart.dto.PointResponse;
import cart.repository.PointRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class PointService {

    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public PointResponse findBy(Member member) {
        Points currentPoints = pointRepository.findUsablePointsByMemberId(member.getId());
        Points toBeExpiredPoints = new Points(currentPoints.getPoints().stream()
                .filter(point -> point.isSoonExpireDate(LocalDate.now()))
                .collect(Collectors.toList()));
        return new PointResponse(currentPoints.getTotalPoint(), toBeExpiredPoints.getTotalPoint());
    }
}
