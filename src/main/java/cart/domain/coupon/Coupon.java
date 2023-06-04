package cart.domain.coupon;

import cart.domain.value.Price;

public interface Coupon {

    Price apply(Price price);

    Long getId();

    String getName();

    String getDiscountPolicyName();

    int getDiscountValue();

    String getCategory();

}
