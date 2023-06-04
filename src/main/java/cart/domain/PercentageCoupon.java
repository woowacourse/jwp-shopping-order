package cart.domain;

import cart.exception.CouponException;

import java.util.Objects;

public class PercentageCoupon implements Coupon {
    private final CouponInfo couponInfo;
    private final Double discountPercentage;

    public PercentageCoupon(final CouponInfo couponInfo, final Double discountPercentage) {
        this.couponInfo = couponInfo;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public boolean isAvailable(final Integer totalPrice) {
        return couponInfo.getMinOrderPrice() <= totalPrice;
    }

    @Override
    public Integer calculateDiscount(final Integer totalPrice) {
        if (couponInfo.getMinOrderPrice() > totalPrice) {
            throw new CouponException.Unavailable("주문 금액이 최소 주문 금액보다 작습니다.");
        }
        double discountPrice = totalPrice * discountPercentage;
        if (discountPrice > couponInfo.getMaxDiscountPrice()) {
            return couponInfo.getMaxDiscountPrice();
        }
        // TODO: 5/29/23 이거 반올림?
        return (int)discountPrice;
    }

    @Override
    public CouponInfo getCouponInfo() {
        return couponInfo;
    }

    @Override
    public Integer getDiscountAmount() {
        return null;
    }

    @Override
    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PercentageCoupon that = (PercentageCoupon) o;
        return Objects.equals(couponInfo, that.couponInfo)
                && Objects.equals(discountPercentage, that.discountPercentage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponInfo, discountPercentage);
    }

    @Override
    public String toString() {
        return "PercentageCoupon{" +
                "couponInfo=" + couponInfo +
                ", discountPercentage=" + discountPercentage +
                '}';
    }
}
