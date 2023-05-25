package cart.domain.discount;

import cart.domain.Member;

public interface PriceCalculator {
    Integer calculateFinalPrice(Integer price, Member member);
}
