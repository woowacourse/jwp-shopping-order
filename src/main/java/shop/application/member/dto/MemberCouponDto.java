package shop.application.member.dto;

import shop.domain.coupon.MemberCoupon;

import java.time.LocalDateTime;

public class MemberCouponDto {
    private Long id;
    private String name;
    private Integer discountRate;
    private LocalDateTime expiredAt;

    private MemberCouponDto(){}

    private MemberCouponDto(Long id, String name, Integer discountRate, LocalDateTime expiredAt) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.expiredAt = expiredAt;
    }

    public static MemberCouponDto of(MemberCoupon memberCoupon) {
        return new MemberCouponDto(
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
