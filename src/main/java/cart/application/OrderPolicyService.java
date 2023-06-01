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
    public OrderPolicyResponse find() {
        ShippingPolicy shippingPolicy = shippingPolicyRepository.findShippingPolicy();
        return new OrderPolicyResponse(shippingPolicy.getThreshold(), shippingPolicy.getBasicShippingFee(), PointPolicy.getPointPercentage());
    }
}
