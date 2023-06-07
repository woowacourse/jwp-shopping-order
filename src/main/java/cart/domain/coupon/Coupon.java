package cart.domain.coupon;

import cart.domain.order.OrderPrice;
import cart.exception.NegativeCouponException;

import java.util.Objects;

public class Coupon {
    private long id;
    private final int discountPrice;
    private final String couponInfo;

    public Coupon(final long id, final int discountPrice, final String couponInfo) {
        validatePrice(discountPrice);
        this.id = id;
        this.discountPrice = discountPrice;
        this.couponInfo = couponInfo;
    }

    public Coupon(final int discountPrice, final String couponInfo) {
        this.discountPrice = discountPrice;
        this.couponInfo = couponInfo;
    }

    private void validatePrice(final int discountPrice) {
        if (discountPrice <= 0) {
            throw new NegativeCouponException("쿠폰의 할인 금액운 0보다 커야했지만 해당 쿠폰의 할인금은 " + discountPrice + "원 입니다.");
        }
    }

    public int calculateDiscount() {
        return discountPrice;
    }

    public OrderPrice apply(final OrderPrice orderPrice) {
        return orderPrice.discountGlobal(discountPrice);
    }

    public long getId() {
        return id;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public String getCouponInfo() {
        return couponInfo;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Coupon coupon = (Coupon) o;
        return id == coupon.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
