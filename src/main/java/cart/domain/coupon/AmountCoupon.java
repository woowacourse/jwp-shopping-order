package cart.domain.coupon;

import cart.exception.CouponException;

import java.util.Objects;

public class AmountCoupon implements Coupon {
    private final CouponInfo couponInfo;
    private final Integer discountAmount;

    public AmountCoupon(final CouponInfo couponInfo, final Integer discountAmount) {
        this.couponInfo = couponInfo;
        this.discountAmount = discountAmount;
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
        return discountAmount;
    }

    @Override
    public CouponInfo getCouponInfo() {
        return couponInfo;
    }

    @Override
    public Integer getDiscountAmount() {
        return discountAmount;
    }

    @Override
    public Double getDiscountPercentage() {
        return null;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AmountCoupon that = (AmountCoupon) o;
        return Objects.equals(couponInfo, that.couponInfo)
                && Objects.equals(discountAmount, that.discountAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponInfo, discountAmount);
    }

    @Override
    public String toString() {
        return "AmountCoupon{" +
                "couponInfo=" + couponInfo +
                ", discountAmount=" + discountAmount +
                '}';
    }
}
