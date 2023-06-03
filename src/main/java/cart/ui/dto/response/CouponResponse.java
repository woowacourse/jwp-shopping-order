package cart.ui.dto.response;

import java.util.Objects;

public class CouponResponse {

    private Long couponId;
    private String couponName;
    private int minAmount;
    private int discountAmount;
    private boolean isPublished;

    public CouponResponse() {
    }

    public CouponResponse(final Long couponId, final String couponName, final int minAmount, final int discountAmount,
        final boolean isPublished) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.minAmount = minAmount;
        this.discountAmount = discountAmount;
        this.isPublished = isPublished;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CouponResponse that = (CouponResponse) o;
        return getMinAmount() == that.getMinAmount() && getDiscountAmount() == that.getDiscountAmount()
            && isPublished == that.isPublished && Objects.equals(getCouponId(), that.getCouponId())
            && Objects.equals(getCouponName(), that.getCouponName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCouponId(), getCouponName(), getMinAmount(), getDiscountAmount(), isPublished);
    }

    public Long getCouponId() {
        return couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public boolean getIsPublished() {
        return isPublished;
    }
}
