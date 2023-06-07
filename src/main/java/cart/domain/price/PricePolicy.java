package cart.domain.price;

import java.util.List;

import cart.domain.Member;
import cart.domain.price.discount.DiscountInformation;
import cart.domain.price.discount.grade.Grade;

public interface PricePolicy {
    Integer computeAdditionalPrice(Integer price, Member member);

    List<DiscountInformation> getAllDiscountPoliciesInformation(Integer price, Grade grade);
}
