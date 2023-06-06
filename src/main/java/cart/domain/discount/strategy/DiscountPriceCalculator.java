package cart.domain.discount.strategy;

import cart.domain.discount.DiscountStep;
import cart.domain.Price;
import org.springframework.stereotype.Component;

@Component
public class DiscountPriceCalculator implements DiscountCalculator {

    @Override
    public Price calculate(final Price price) {
        return DiscountStep.findDiscountAmount(price);
    }
}
