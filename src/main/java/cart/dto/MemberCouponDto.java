package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 쿠폰")
public class MemberCouponDto {

    @Schema(description = "쿠폰 Id", example = "1")
    private final Long id;

    @Schema(description = "쿠폰명", example = "40000원 이상 3000원 할인 쿠폰")
    private final String name;

    @Schema(description = "쿠폰타입", example = "PRICE")
    private final String type;

    @Schema(description = "할인가격", example = "3000")
    private final Long discountPrice;

    @Schema(description = "쿠폰적용조건", example = "40000")
    private final Long minimumPrice;

    public MemberCouponDto(final Long id, final String name, final String type, final Long discountPrice,
                           final Long minimumPrice) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.discountPrice = discountPrice;
        this.minimumPrice = minimumPrice;
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

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public Long getMinimumPrice() {
        return minimumPrice;
    }
}
