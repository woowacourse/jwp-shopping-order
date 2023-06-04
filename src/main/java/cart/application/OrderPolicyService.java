package cart.application;

import cart.domain.point.PointPolicyStrategy;
import cart.dto.orderpolicy.OrderPolicyResponse;
import cart.repository.OrderPolicyRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderPolicyService {

    private final OrderPolicyRepository orderPolicyRepository;
    private final PointPolicyStrategy pointPolicyStrategy;

    public OrderPolicyService(OrderPolicyRepository orderPolicyRepository, PointPolicyStrategy pointPolicyStrategy) {
        this.orderPolicyRepository = orderPolicyRepository;
        this.pointPolicyStrategy = pointPolicyStrategy;
    }

    public OrderPolicyResponse findOrderPolicy() {
        final Long threshold = orderPolicyRepository.findThreshold();
        final Long fee = orderPolicyRepository.findFee();
        final int pointPercentage = pointPolicyStrategy.getPointPercentage();
        return new OrderPolicyResponse(threshold, fee, pointPercentage);
    }
}
