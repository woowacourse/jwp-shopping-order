package cart.ui.coupon.dto;

import cart.application.service.coupon.dto.CouponResultDto;

public class CouponResponse {

    private final Long id;
    private final String couponName;
    private final Integer minAmount;
    private final Integer discountPercent;
    private final Integer discountAmount;

    private CouponResponse(final Long id,
                           final String couponName,
                           final Integer minAmount,
                           final Integer discountPercent,
                           final Integer discountAmount
    ) {
        this.id = id;
        this.couponName = couponName;
        this.minAmount = minAmount;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
    }

    public static CouponResponse from(final CouponResultDto couponResultDto) {
        return new CouponResponse(
                couponResultDto.getId(),
                couponResultDto.getCouponName(),
                couponResultDto.getMinAmount(),
                couponResultDto.getDiscountPercent(),
                couponResultDto.getDiscountAmount()
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

    public Integer getDiscountAmount() {
        return discountAmount;
    }

}
