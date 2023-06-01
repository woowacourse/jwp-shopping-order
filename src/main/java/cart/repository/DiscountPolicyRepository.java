package cart.repository;

import cart.domain.DiscountPolicy;
import java.util.Optional;

public interface DiscountPolicyRepository {
    Optional<DiscountPolicy> findDefault();
}
