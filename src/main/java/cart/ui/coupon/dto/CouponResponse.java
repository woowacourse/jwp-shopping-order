package cart.ui.coupon.dto;

import cart.application.service.coupon.CouponResultDto;

public class CouponResponse {

    private final Long id;
    private final String couponName;
    private final Integer discountPercent;
    private final Integer discountAmount;
    private final Integer minAmount;

    public CouponResponse(Long id, String couponName, Integer discountPercent, Integer discountAmount, Integer minAmount) {
        this.id = id;
        this.couponName = couponName;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.minAmount = minAmount;
    }

    public static CouponResponse from(CouponResultDto couponResultDto) {
        return new CouponResponse(
                couponResultDto.getId(),
                couponResultDto.getCouponName(),
                couponResultDto.getDiscountPercent(),
                couponResultDto.getDiscountAmount(),
                couponResultDto.getMinAmount()
        );
    }

    public Long getId() {
        return id;
    }

    public String getCouponName() {
        return couponName;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

}
