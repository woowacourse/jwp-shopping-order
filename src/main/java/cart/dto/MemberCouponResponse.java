package cart.dto;

import cart.domain.memberCoupon.MemberCoupon;

public class MemberCouponResponse {
    private final Long id;
    private final String name;
    private final String discountType;
    private final Double discountRate;
    private final Integer discountAmount;
    private final Integer minimumPrice;

    public MemberCouponResponse(Long id, String name, String discountType, Double discountRate, Integer discountAmount, Integer minimumPrice) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.minimumPrice = minimumPrice;
    }

    public static MemberCouponResponse from(MemberCoupon memberCoupon) {
        return new MemberCouponResponse(
                memberCoupon.getId(),
                memberCoupon.getCoupon().getName(),
                memberCoupon.getCoupon().getDiscountType().getName(),
                memberCoupon.getCoupon().getDiscountPercent(),
                memberCoupon.getCoupon().getDiscountAmount(),
                memberCoupon.getCoupon().getMinimumPrice()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscountType() {
        return discountType;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Integer getMinimumPrice() {
        return minimumPrice;
    }
}
