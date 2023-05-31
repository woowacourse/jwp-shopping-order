package cart.dto;

import cart.domain.coupon.Coupon;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "쿠폰")
public class MemberCouponResponse {

    @Schema(description = "쿠폰 id", example = "1")
    private final Long id;

    @Schema(description = "쿠폰명", example = "30000원 이상 3000원 할인 쿠폰")
    private final String name;

    @Schema(description = "쿠폰 타입", example = "price")
    private final String type;

    @Schema(description = "할인 값", example = "3000")
    private final long value;

    @Schema(description = "최소 금액 할인 조건", example = "30000")
    private final long minimumPrice;

    public MemberCouponResponse(final Long id,
                                final String name,
                                final String type,
                                final long value,
                                final long minimumPrice) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
        this.minimumPrice = minimumPrice;
    }

    public static MemberCouponResponse of(final Long memberCouponId, final Coupon coupon) {
        return new MemberCouponResponse(
                memberCouponId,
                coupon.getName(),
                coupon.getDiscountPolicyType().name().toLowerCase(),
                coupon.getDiscountValue(),
                coupon.getMinimumPrice().getLongValue()
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

    public long getValue() {
        return value;
    }

    public long getMinimumPrice() {
        return minimumPrice;
    }
}
