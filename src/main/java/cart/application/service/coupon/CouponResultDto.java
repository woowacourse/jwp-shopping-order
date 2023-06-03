package cart.application.service.coupon;

import cart.application.service.coupon.dto.MemberCouponDto;

public class CouponResultDto {
    private final long id;
    private final String couponName;
    private final int discountPercent;
    private final int discountAmount;
    private final int minAmount;

    private CouponResultDto(Long id, String couponName, Integer discountPercent, int discountAmount, Integer minAmount) {
        this.id = id;
        this.couponName = couponName;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.minAmount = minAmount;
    }

    public static CouponResultDto from(MemberCouponDto memberCouponDto) {
        return new CouponResultDto(
                memberCouponDto.getId(),
                memberCouponDto.getCouponName(),
                memberCouponDto.getDiscountPercent(),
                memberCouponDto.getDiscountAmount(),
                memberCouponDto.getMinAmount()
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

    public int getDiscountAmount() {
        return discountAmount;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

}
