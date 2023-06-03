package cart.dto.coupon;

import cart.domain.VO.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.DiscountPolicyType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Schema(description = "쿠폰 저장")
public class CouponSaveRequest {

    @Schema(description = "쿠폰명", example = "3000원 할인 쿠폰")
    @NotBlank(message = "쿠폰명을 입력해야 합니다")
    private final String name;

    @Schema(description = "쿠폰 타입", example = "PRICE")
    @NotBlank(message = "쿠폰타입을 입력해야 합니다")
    private final String type;

    @Schema(description = "할인 값", example = "3000")
    @Positive(message = "할인 값은 1 이상이어야 합니다.")
    private final long value;

    @Schema(description = "최소 금액 할인 조건", example = "0")
    @PositiveOrZero(message = "최소 금액 할인 조건은 0 이상이어야 합니다.")
    private final Long minimumPrice;

    public CouponSaveRequest(final String name, final String type, final Long value, final Long minimumPrice) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.minimumPrice = minimumPrice;
    }

    public Coupon toDomain() {
        final DiscountPolicyType type = DiscountPolicyType.from(this.type);
        return new Coupon(name, type, value, Money.from(minimumPrice));
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Long getValue() {
        return value;
    }

    public Long getMinimumPrice() {
        return minimumPrice;
    }
}
