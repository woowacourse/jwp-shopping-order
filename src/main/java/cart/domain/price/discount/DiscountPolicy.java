package cart.domain.price.discount;

import cart.domain.Member;
import cart.domain.price.discount.grade.Grade;

public interface DiscountPolicy {
    Integer calculateDiscountPrice(Integer price, Member member);

    DiscountInformation computeDiscountInformation(Integer price, Grade grade);
}
