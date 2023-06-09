package cart.domain.coupon;

import cart.domain.value.Price;

public interface Coupon {

    boolean isSupport(Category category);

    Price apply(Price price);

    Long getId();

    String getName();

    String getDiscountPolicyName();

    int getDiscountValue();

    Category getCategory();

}
