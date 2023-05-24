package cart.domain.discount.grade;

import cart.domain.Member;
import cart.domain.discount.DiscountPolicy;

public class GradeDiscountPolicy implements DiscountPolicy {
    @Override
    public Integer calculateDiscountPrice(Integer price, Member member) {
        final Grade grade = Grade.findGradeByGradeValue(member.getGrade());
        return grade.calculate(price);
    }
}
