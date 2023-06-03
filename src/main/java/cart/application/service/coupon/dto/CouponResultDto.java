package cart.application.service.coupon.dto;

import cart.domain.coupon.Coupon;

public class CouponResultDto {
    private final long id;
    private final String couponName;
    private final int minAmount;
    private final int discountPercent;
    private final int discountAmount;

    private CouponResultDto(final Long id,
                            final String couponName,
                            final Integer minAmount,
                            final Integer discountPercent,
                            final int discountAmount) {
        this.id = id;
        this.couponName = couponName;
        this.minAmount = minAmount;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
    }

    public static CouponResultDto from(final Coupon coupon) {
        return new CouponResultDto(
                coupon.getId(),
                coupon.getCouponName(),
                coupon.getMinAmount(),
                coupon.getDiscountPercent(),
                coupon.getDiscountAmount()
        );
    }

    public Long getId() {
        return id;
    }

    public String getCouponName() {
        return couponName;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

}
