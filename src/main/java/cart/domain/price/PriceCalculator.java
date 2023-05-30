package cart.domain.price;

import cart.domain.Member;

public interface PriceCalculator {
    Integer calculateFinalPrice(Integer price, Member member);
}
