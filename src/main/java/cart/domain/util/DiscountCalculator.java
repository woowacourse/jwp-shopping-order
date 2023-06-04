package cart.domain.util;

import cart.domain.Coupon;
import java.util.Optional;

public class DiscountCalculator {

    private static final Double PERCENTAGE = 100.0;

    private DiscountCalculator() {
    }

    public static int calculatePriceAfterDiscount(final Integer originPrice,
                                                  final Optional<Coupon> coupon) {
        return coupon.map(notNullCoupon -> applyDiscount(originPrice, notNullCoupon))
                .orElseGet(() ->  originPrice);
    }

    private static int applyDiscount(final Integer originPrice,
                                     final Coupon coupon) {
        int priceAfterDiscount = originPrice;
        priceAfterDiscount -= (int) (priceAfterDiscount * (coupon.getDiscountRate() / PERCENTAGE));
        priceAfterDiscount -= coupon.getDiscountPrice();
        return priceAfterDiscount;
    }

}
