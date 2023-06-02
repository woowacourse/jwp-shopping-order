package cart.application;

import cart.domain.PointPolicy;
import cart.domain.ShippingPolicy;
import cart.dto.response.OrderPolicyResponse;
import cart.repository.ShippingPolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderPolicyService {

    private final ShippingPolicyRepository shippingPolicyRepository;

    public OrderPolicyService(final ShippingPolicyRepository shippingPolicyRepository) {
        this.shippingPolicyRepository = shippingPolicyRepository;
    }

    @Transactional(readOnly = true)
    public OrderPolicyResponse findTotalPolicy() {
        ShippingPolicy shippingPolicy = findShippingPolicy();
        PointPolicy pointPolicy = findPointPolicy();
        return new OrderPolicyResponse(shippingPolicy.getThreshold(), shippingPolicy.getBasicShippingFee(), pointPolicy.getPointPercentage());
    }

    public PointPolicy findPointPolicy() {
        return new PointPolicy();
    }

    @Transactional(readOnly = true)
    public ShippingPolicy findShippingPolicy() {
        return shippingPolicyRepository.findShippingPolicy();
    }
}