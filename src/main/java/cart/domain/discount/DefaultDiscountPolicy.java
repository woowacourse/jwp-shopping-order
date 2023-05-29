package cart.domain.discount;

import org.springframework.stereotype.Component;

import cart.domain.Member;
import cart.domain.discount.grade.GradeDiscountPolicy;
import cart.domain.discount.price.PriceDiscountPolicy;

@Component
public class DefaultDiscountPolicy implements DiscountPolicy{
    private final GradeDiscountPolicy gradeDiscountPolicy;
    private final PriceDiscountPolicy priceDiscountPolicy;

    public DefaultDiscountPolicy(
            GradeDiscountPolicy gradeDiscountPolicy,
            PriceDiscountPolicy priceDiscountPolicy
    ) {
        this.gradeDiscountPolicy = gradeDiscountPolicy;
        this.priceDiscountPolicy = priceDiscountPolicy;
    }

    @Override
    public Integer calculateDiscountPrice(Integer price, Member member) {
        return gradeDiscountPolicy.calculateDiscountPrice(price, member)
                + priceDiscountPolicy.calculateDiscountPrice(price);
    }
}
