package cart.ui.member.dto;

import cart.domain.coupon.MemberCoupon;

import java.time.LocalDateTime;

public class MemberCouponResponse {
    private final Long id;
    private final String name;
    private final Integer discountRate;
    private final LocalDateTime expiredAt;

    private MemberCouponResponse(Long id, String name, Integer discountRate, LocalDateTime expiredAt) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.expiredAt = expiredAt;
    }

    public static MemberCouponResponse of(MemberCoupon memberCoupon) {
        return new MemberCouponResponse(
                memberCoupon.getId(),
                memberCoupon.getCouponName(),
                memberCoupon.getDiscountRate(),
                memberCoupon.getMemberCouponExpiredAt()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
