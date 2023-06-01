package cart.domain.price.discount.grade;

import org.springframework.stereotype.Component;

import cart.domain.Member;
import cart.domain.price.discount.DiscountInformation;
import cart.domain.price.discount.DiscountPolicy;

@Component
public class BasicGradeDiscountPolicy implements DiscountPolicy {
    private static final String NAME = "memberGradeDiscount";

    @Override
    public Integer calculateDiscountPrice(Integer price, Member member) {
        final Grade grade = Grade.findGradeByGradeValue(member.getGrade());
        return grade.calculate(price);
    }

    @Override
    public DiscountInformation computeDiscountInformation(Integer price, Grade grade) {
        return new DiscountInformation(NAME, grade.getDiscountRate(), grade.calculate(price));
    }
}
