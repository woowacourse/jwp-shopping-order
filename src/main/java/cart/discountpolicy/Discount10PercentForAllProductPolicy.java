package cart.discountpolicy;

import cart.discountpolicy.discountcondition.Category;
import cart.discountpolicy.discountcondition.DiscountCondition;
import org.springframework.stereotype.Component;

@Component
public class Discount10PercentForAllProductPolicy implements DiscountPolicy {
    @Override
    public boolean isSelective() {
        return true;
    }

    @Override
    public boolean support(DiscountCondition condition) {
        return true;
    }

    @Override
    public int discount(int price) {
        return Double.valueOf(Math.floor(price * 0.9)).intValue();
    }
}
