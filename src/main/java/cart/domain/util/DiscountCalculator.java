package cart.domain.util;

import cart.domain.Coupon;

public class DiscountCalculator {

    private static final Double PERCENTAGE = 100.0;

    private DiscountCalculator() {
    }

    public static int calculatePriceAfterDiscount(final Integer originalPrice,
                                                  final Coupon coupon) {
        int priceAfterDiscount = originalPrice;
        priceAfterDiscount -= (int) (priceAfterDiscount * (coupon.getDiscountRate() / PERCENTAGE));
        priceAfterDiscount -= coupon.getDiscountPrice();
        return priceAfterDiscount;
    }

}
