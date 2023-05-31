package cart.dto;

import cart.domain.member.MemberCoupon;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 쿠폰")
public class MemberCouponResponse {

    @Schema(description = "사용자 쿠폰 id", example = "1")
    private final Long id;
    @Schema(description = "이름", example = "생일 쿠폰")
    private final String name;
    @Schema(description = "쿠폰 종류", example = "PRICE")
    private final String type;
    @Schema(description = "할인 가격", example = "3000")
    private final long value;
    @Schema(description = "최소 금액", example = "50000")
    private final long minimumPrice;

    public MemberCouponResponse(final Long id, final String name, final String type, final long value, final long minimumPrice) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
        this.minimumPrice = minimumPrice;
    }

    public static MemberCouponResponse from(final MemberCoupon memberCoupon) {
        return new MemberCouponResponse(
                memberCoupon.getId(),
                memberCoupon.getCoupon().getName(),
                memberCoupon.getCoupon().getDiscountPolicy().getName(),
                memberCoupon.getCoupon().getValue(),
                memberCoupon.getCoupon().getMinimumPrice().getValue()
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
