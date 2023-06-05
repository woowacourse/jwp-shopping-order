package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Schema(description = "쿠폰 저장 Request")
public class CouponSaveRequest {

    @Schema(description = "쿠폰명", example = "10000원 이상 10% 할인 쿠폰")
    @NotBlank(message = "쿠폰명은 공백일 수 없습니다.")
    private final String name;

    @Schema(description = "쿠폰타입", example = "RATE")
    @NotBlank(message = "쿠폰타입은 공백일 수 없습니다.")
    private final String discountPolicyType;

    @Schema(description = "할인정도(가격, %)", example = "10")
    @PositiveOrZero(message = "할인정도는 최소 0 이상입니다.")
    private final Long discountValue;

    @Schema(description = "쿠폰적용조건", example = "10000")
    @PositiveOrZero(message = "쿠폰적용조건은 최소 0원 이상입니다.")
    private final Long minimumPrice;

    public CouponSaveRequest(final String name, final String discountPolicyType, final Long discountValue,
                             final Long minimumPrice) {
        this.name = name;
        this.discountPolicyType = discountPolicyType;
        this.discountValue = discountValue;
        this.minimumPrice = minimumPrice;
    }

    public String getName() {
        return name;
    }

    public String getDiscountPolicyType() {
        return discountPolicyType;
    }

    public Long getDiscountValue() {
        return discountValue;
    }

    public Long getMinimumPrice() {
        return minimumPrice;
    }
}
