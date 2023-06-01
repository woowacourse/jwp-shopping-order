package cart.dto;

import cart.domain.Coupon;
import cart.domain.MemberCoupon;

import java.time.LocalDateTime;

public class MemberCouponResponse {

    private Long id;
    private String name;
    private int discountRate;
    private LocalDateTime expiredAt;
    private boolean isUsed;

    public MemberCouponResponse() {
    }

    private MemberCouponResponse(final Long id, final String name, final int discountRate, final LocalDateTime expiredAt, final boolean isUsed) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.expiredAt = expiredAt;
        this.isUsed = isUsed;
    }

    public static MemberCouponResponse from(final MemberCoupon memberCoupon) {
        Coupon coupon = memberCoupon.getCoupon();
        return new MemberCouponResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountRate(),
                memberCoupon.getExpiredAt(),
                memberCoupon.isUsed()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public boolean getIsUsed() {
        return isUsed;
    }
}
