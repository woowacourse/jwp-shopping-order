package cart.dto;

import cart.domain.coupon.Coupon;

public class CouponResponse {

    private final Long id;
    private final String name;
    private final String type;
    private final long discountPrice;
    private final int discountPercent;
    private final boolean discountDeliveryFee;
    private final long minimumPrice;

    public CouponResponse(final Long id, final String name, final String type, final long discountPrice, final int discountPercent, final boolean discountDeliveryFee, final long minimumPrice) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.discountPrice = discountPrice;
        this.discountPercent = discountPercent;
        this.discountDeliveryFee = discountDeliveryFee;
        this.minimumPrice = minimumPrice;
    }

    public static CouponResponse from(final Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountPolicy().getName(),
                coupon.getDiscountPolicy().getDiscountPrice(),
                coupon.getDiscountPolicy().getDiscountPercent(),
                coupon.getDiscountPolicy().isDiscountDeliveryFee(),
                coupon.getDiscountCondition().getMinimumPrice()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public boolean isDiscountDeliveryFee() {
        return discountDeliveryFee;
    }

    public long getMinimumPrice() {
        return minimumPrice;
    }
}
