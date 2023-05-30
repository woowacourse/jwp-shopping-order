package cart.domain.price;

import org.springframework.stereotype.Component;

import cart.domain.Member;

@Component
public class DiscountPriceCalculator implements PriceCalculator {
    private final PricePolicy pricePolicy;

    public DiscountPriceCalculator(PricePolicy pricePolicy) {
        this.pricePolicy = pricePolicy;
    }

    @Override
    public Integer calculateFinalPrice(Integer price, Member member) {
        return price - pricePolicy.computeAdditionalPrice(price, member);
    }
}
