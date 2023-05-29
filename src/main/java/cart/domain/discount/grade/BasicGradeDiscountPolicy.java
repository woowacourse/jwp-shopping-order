package cart.domain.discount.grade;

import org.springframework.stereotype.Component;

import cart.domain.Member;

@Component
public class BasicGradeDiscountPolicy implements GradeDiscountPolicy {
    @Override
    public Integer calculateDiscountPrice(Integer price, Member member) {
        final Grade grade = Grade.findGradeByGradeValue(member.getGrade());
        return grade.calculate(price);
    }
}
