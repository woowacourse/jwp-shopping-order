package cart.discountpolicy;

import cart.discountpolicy.discountcondition.Category;
import cart.discountpolicy.discountcondition.DiscountCondition;
import org.springframework.stereotype.Component;

@Component
public class Pizza5000DiscountPolicy implements DiscountPolicy {
    @Override
    public boolean isSelective() {
        return false;
    }

    @Override
    public boolean support(DiscountCondition condition) {
        return condition.getCategory() == Category.PIZZA;
    }

    @Override
    public int discount(int price) {
        return 5_000;
    }
}
