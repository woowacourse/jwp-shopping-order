package cart.application;

import cart.domain.MemberGrade;
import cart.domain.discount.MemberGradeDiscountPolicy;
import cart.domain.discount.PriceDiscountPolicy;
import cart.dto.DiscountInformationResponse;
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

    public List<DiscountInformationResponse> getDiscountInfo(final MemberGrade grade, final int totalPrice) {
        final MemberGradeDiscountPolicy gradeDiscount = new MemberGradeDiscountPolicy(grade);
        final PriceDiscountPolicy priceDiscount = new PriceDiscountPolicy();

        final DiscountInformationResponse gradeResponse = new DiscountInformationResponse(gradeDiscount.getName(),
                gradeDiscount.getRate(),
                gradeDiscount.calculateDiscountAmount(totalPrice));
        final DiscountInformationResponse priceResponse = new DiscountInformationResponse(priceDiscount.getName(),
                gradeDiscount.getRate(),
                gradeDiscount.calculateDiscountAmount(totalPrice));
        return List.of(gradeResponse, priceResponse);
    }
}
