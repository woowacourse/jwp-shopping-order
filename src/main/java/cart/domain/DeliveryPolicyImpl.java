package cart.domain;

import org.springframework.stereotype.Component;

@Component
public class DeliveryPolicyImpl implements DeliveryPolicy {
    @Override
    public Money calculate(Order order) {
        return Money.from(3000);
    }
}
