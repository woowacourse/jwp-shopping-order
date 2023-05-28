package cart.discountpolicy;

import cart.discountpolicy.discountcondition.DiscountCondition;
import org.springframework.stereotype.Component;

@Component
public class FreeDeliveryPolicy implements DiscountPolicy {
    @Override
    public boolean isSelective() {
        return true;
    }

    @Override
    public boolean support(DiscountCondition condition) {
        return false;
    }

    @Override
    public int discount(int price) {
        return 0;
    }
}
