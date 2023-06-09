package cart.repository;

import cart.domain.DeliveryPolicy;
import java.util.List;

public interface DeliveryPolicyRepository {

    List<DeliveryPolicy> findDefault();
}
