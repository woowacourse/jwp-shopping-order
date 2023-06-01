package cart.repository;

import cart.domain.DeliveryPolicy;
import java.util.Optional;

public interface DeliveryPolicyRepository {
    Optional<DeliveryPolicy> findDefault();
}
