package cart.domain.discount.grade;

import cart.domain.Member;

public interface GradeDiscountPolicy {
    Integer calculateDiscountPrice(Integer price, Member member);
}
