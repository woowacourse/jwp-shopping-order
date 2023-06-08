package cart.application.dto.coupon;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CouponRequest {

    @NotBlank(message = "쿠폰 이름은 비어있을 수 없습니다.")
    private final String name;

    @NotNull(message = "쿠폰 할인율은 비어있을 수 없습니다.")
    private final Integer discountRate;

    @NotNull(message = "쿠폰 기간은 비어있을 수 없습니다.")
    private final Integer period;

    public CouponRequest() {
        this(null, null, null);
    }

    public CouponRequest(final String name, final Integer discountRate, final Integer period) {
        this.name = name;
        this.discountRate = discountRate;
        this.period = period;
    }

    public String getName() {
        return name;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public Integer getPeriod() {
        return period;
    }
}
