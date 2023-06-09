package cart.repository;

import cart.domain.DiscountPolicy;
import java.util.List;

public interface DiscountPolicyRepository {
    List<DiscountPolicy> findDefault();
}
