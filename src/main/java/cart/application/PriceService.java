package cart.application;

import java.util.List;

import org.springframework.stereotype.Service;

import cart.domain.price.PricePolicy;
import cart.domain.price.discount.DiscountInformation;
import cart.domain.price.discount.grade.Grade;
import cart.dto.TotalDiscountInfoResponse;

@Service
public class PriceService {
    private final PricePolicy pricePolicy;

    public PriceService(PricePolicy pricePolicy) {
        this.pricePolicy = pricePolicy;
    }

    public TotalDiscountInfoResponse getDiscountInformation(Integer price, String gradeName) {
        final Grade grade = Grade.findGradeByGradeName(gradeName);
        final List<DiscountInformation> discountPoliciesInformations = pricePolicy.getAllDiscountPoliciesInformation(
                price, grade);
        return TotalDiscountInfoResponse.of(discountPoliciesInformations);
    }
}
