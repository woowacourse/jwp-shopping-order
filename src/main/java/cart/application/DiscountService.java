package cart.application;

import cart.domain.Grade;
import cart.domain.PriceDiscountCalculator;
import cart.dto.response.DiscountResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    public List<DiscountResponse> getPrice(final int price, final String gradeName) {
        final List<DiscountResponse> discountResponses = new ArrayList<>();

        final Grade grade = Grade.valueOf(gradeName.toUpperCase());
        final int discountedPriceByGrade = grade.calculateGradeDiscountPrice(price);

        final DiscountResponse gradeDiscountResponse = DiscountResponse.of(grade, discountedPriceByGrade);

        discountResponses.add(gradeDiscountResponse);

        final PriceDiscountCalculator priceDiscountCalculator = new PriceDiscountCalculator(price);
        final DiscountResponse priceDiscountResponse = DiscountResponse.from(priceDiscountCalculator);

        discountResponses.add(priceDiscountResponse);
        return discountResponses;
    }
}
