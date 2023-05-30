package cart.discountpolicy.application;

import cart.discountpolicy.DiscountPolicy;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DiscountPolicyRepository {
    private final Map<Long, DiscountPolicy> discountPolicyMap = new HashMap<>();
    private long id = 0L;

    public long save(DiscountPolicy discountPolicy) {
        final var id = this.id++;
        discountPolicyMap.put(id, discountPolicy);
        return id;
    }

    public DiscountPolicy findById(Long id) {
        return this.discountPolicyMap.get(id);
    }
}
