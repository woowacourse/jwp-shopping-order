package cart.application;

import cart.domain.Member;
import cart.dto.UserResponse;
import cart.repository.PointRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PointRepository pointRepository;

    public UserService(final PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public UserResponse getUserResponse(final Member member) {
        return new UserResponse(
                member.getEmail(),
                pointRepository.getTotalLeftPoint(member).getValue(),
                pointRepository.getEarningRate()
        );
    }
}
