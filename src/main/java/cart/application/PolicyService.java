package cart.application;

import cart.dto.DefaultDeliveryPolicyResponse;
import cart.dto.DefaultDiscountPolicyResponse;
import cart.repository.DeliveryPolicyRepository;
import cart.repository.DiscountPolicyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PolicyService {

    private final DiscountPolicyRepository discountPolicyRepository;
    private final DeliveryPolicyRepository deliveryPolicyRepository;

    public PolicyService(final DiscountPolicyRepository discountPolicyRepository, final DeliveryPolicyRepository deliveryPolicyRepository) {
        this.discountPolicyRepository = discountPolicyRepository;
        this.deliveryPolicyRepository = deliveryPolicyRepository;
    }

    public List<DefaultDiscountPolicyResponse> getDefaultDiscountPolicy() {
        return this.discountPolicyRepository.findDefault().stream()
                .map(DefaultDiscountPolicyResponse::from)
                .collect(Collectors.toList());
    }

    public List<DefaultDeliveryPolicyResponse> getDefaultDeliveryPolicy() {
        return this.deliveryPolicyRepository.findDefault().stream()
                .map(DefaultDeliveryPolicyResponse::from)
                .collect(Collectors.toList());
    }
}
