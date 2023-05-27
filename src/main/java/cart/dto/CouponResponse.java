package cart.dto;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.DiscountCondition;
import cart.domain.coupon.DiscountPolicy;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "쿠폰")
public class CouponResponse {

    @Schema(description = "쿠폰 id", example = "1")
    private final Long id;

    @Schema(description = "쿠폰명", example = "30000원 이상 3000원 할인 쿠폰")
    private final String name;

    @Schema(description = "쿠폰 타입", example = "price")
    private final String type;

    @Schema(description = "할인 금액", example = "3000")
    private final long discountPrice;

    @Schema(description = "할인율", example = "0")
    private final int discountPercent;

    @Schema(description = "배달비 할인 여부", example = "false")
    private final boolean discountDeliveryFee;

    @Schema(description = "최소 금액 할인 조건", example = "30000")
    private final long minimumPrice;

    public CouponResponse(
            final Long id,
            final String name,
            final String type,
            final long discountPrice,
            final int discountPercent,
            final boolean discountDeliveryFee,
            final long minimumPrice
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.discountPrice = discountPrice;
        this.discountPercent = discountPercent;
        this.discountDeliveryFee = discountDeliveryFee;
        this.minimumPrice = minimumPrice;
    }

    public static CouponResponse from(final Coupon coupon) {
        final DiscountPolicy discountPolicy = coupon.getDiscountPolicy();
        final DiscountCondition discountCondition = coupon.getDiscountCondition();
        return new CouponResponse(coupon.getId(),
                coupon.getName(),
                discountPolicy.getDiscountPolicyType().name().toLowerCase(),
                discountPolicy.getDiscountPrice().getLongValue(),
                discountPolicy.getDiscountPercent(),
                discountPolicy.isDiscountDeliveryFee(),
                discountCondition.getMinimumPrice().getLongValue()
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
