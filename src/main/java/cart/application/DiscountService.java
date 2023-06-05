package cart.application;

import cart.domain.MemberGrade;
import cart.domain.discount.MemberGradeDiscountPolicy;
import cart.domain.discount.PriceDiscountPolicy;
import cart.dto.DiscountResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountService {
    public int calculateTotalPrice(final MemberGrade grade, final int totalPrice) {
        final int discountAmount = new MemberGradeDiscountPolicy(grade)
                .and(new PriceDiscountPolicy())
                .calculateDiscountAmount(totalPrice);

        return totalPrice - discountAmount;
    }

    public List<DiscountResponse> getDiscountInfo(final MemberGrade grade, final int totalPrice) {
        final MemberGradeDiscountPolicy gradeDiscount = new MemberGradeDiscountPolicy(grade);
        final PriceDiscountPolicy priceDiscount = new PriceDiscountPolicy();

        final DiscountResponse gradeResponse = new DiscountResponse(gradeDiscount.getName(),
                gradeDiscount.getRate(),
                gradeDiscount.calculateDiscountAmount(totalPrice));
        final DiscountResponse priceResponse = new DiscountResponse(priceDiscount.getName(),
                priceDiscount.getRate(totalPrice),
                priceDiscount.calculateDiscountAmount(totalPrice));
        return List.of(gradeResponse, priceResponse);
    }
}
