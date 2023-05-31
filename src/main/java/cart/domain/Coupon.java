package cart.domain;

import cart.exception.CouponDiscountOverPriceException;
import cart.exception.NegativeCouponException;

public class Coupon {
    private final long id;
    private final int discountPrice;
    private final String couponInfo;

    public Coupon(final long id, final int discountPrice, final String couponInfo) {
        validatePrice(discountPrice);
        this.id = id;
        this.discountPrice = discountPrice;
        this.couponInfo = couponInfo;
    }

    private void validatePrice(final int discountPrice) {
        if (discountPrice <= 0) {
            throw new NegativeCouponException("쿠폰의 할인 금액운 0보다 커야했지만 해당 쿠폰의 할인금은 " + discountPrice + "원 입니다.");
        }
    }

    public int discount(final int price) {
        if (price < discountPrice) {
            throw new CouponDiscountOverPriceException("쿠폰 할인 금액이 상품 계산 값보다 더 큽니다.");
        }
        return price - discountPrice;
    }
}
