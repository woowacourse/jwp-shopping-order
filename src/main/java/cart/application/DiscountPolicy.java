package cart.application;

import cart.domain.AmountDiscountRange;
import org.springframework.stereotype.Service;

@Service
public class DiscountPolicy {

    public int discountAmountByPrice(final int price) {
        final int discountAmount = AmountDiscountRange.findDiscountAmount(price);

        return price - discountAmount;
    }
}
