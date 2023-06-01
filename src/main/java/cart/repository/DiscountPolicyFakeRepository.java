package cart.repository;

import cart.domain.DefaultDiscountPolicy;
import cart.domain.DiscountPolicy;
import cart.domain.Money;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DiscountPolicyFakeRepository implements DiscountPolicyRepository {

    List<DiscountPolicy> discountPolicies = List.of(
            new DefaultDiscountPolicy("5만원 이상 구매 시 10% 할인", Money.from(50_000), 0.1));

    @Override
    public Optional<DiscountPolicy> findDefault() {
        return Optional.ofNullable(discountPolicies.get(0));
    }
}
