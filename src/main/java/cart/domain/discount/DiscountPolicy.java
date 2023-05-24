package cart.domain.discount;

import cart.domain.Member;

public interface DiscountPolicy {
    Integer calculateDiscountPrice(Integer price, Member member);
}
