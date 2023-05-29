package cart.dto;

import cart.domain.coupon.Coupon;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "쿠폰")
public class CouponResponse {

    @Schema(description = "쿠폰 id", example = "1")
    private final Long id;
    @Schema(description = "이름", example = "생일 쿠폰")
    private final String name;
    @Schema(description = "쿠폰 종류", example = "PRICE")
    private final String type;
    @Schema(description = "할인 가격", example = "3000")
    private final long discountPrice;
    @Schema(description = "할인률", example = "10")
    private final int discountPercent;
    @Schema(description = "배달비 할인 여부", example = "true")
    private final boolean discountDeliveryFee;
    @Schema(description = "최소 금액", example = "50000")
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
