package cart.repository;

import cart.domain.DefaultDeliveryPolicy;
import cart.domain.DeliveryPolicy;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryPolicyFakeRepository implements DeliveryPolicyRepository {

    List<DeliveryPolicy> deliveryPolicies = List.of(new DefaultDeliveryPolicy());

    @Override
    public Optional<DeliveryPolicy> findDefault() {
        return Optional.ofNullable(deliveryPolicies.get(0));
    }
}
