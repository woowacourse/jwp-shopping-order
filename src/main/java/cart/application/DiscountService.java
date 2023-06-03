package cart.application;

import cart.domain.Grade;
import cart.domain.discount.Discount;
import cart.domain.discount.GradeDiscount;
import cart.domain.discount.PriceDiscount;
import cart.dto.response.DiscountResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    public List<DiscountResponse> getPrice(final int price, final String gradeName) {
        final List<DiscountResponse> discountResponses = new ArrayList<>();

        final Discount gradeDiscount = GradeDiscount.of(Grade.from(gradeName), price);
        final DiscountResponse gradeDiscountResponse = DiscountResponse.from(gradeDiscount);

        discountResponses.add(gradeDiscountResponse);

        final Discount priceDiscount = new PriceDiscount(price);
        final DiscountResponse priceDiscountResponse = DiscountResponse.from(priceDiscount);

        discountResponses.add(priceDiscountResponse);
        return discountResponses;
    }
}
