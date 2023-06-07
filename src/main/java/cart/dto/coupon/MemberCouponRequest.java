package cart.dto.coupon;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class MemberCouponRequest {

    @NotNull(message = "ID는 필수 입력입니다. 반드시 입력해주세요.")
    @Positive(message = "유효한 ID를 입력해주세요.")
    private Long couponId;
    @NotBlank(message = "이름은 필수 입력입니다. 반드시 입력해주세요.")
    private String name;
    private DiscountRequest discount;

    private MemberCouponRequest() {
    }

    public MemberCouponRequest(final Long couponId, final String name, final DiscountRequest discount) {
        this.couponId = couponId;
        this.name = name;
        this.discount = discount;
    }

    public Long getCouponId() {
        return couponId;
    }

    public String getName() {
        return name;
    }

    public DiscountRequest getDiscount() {
        return discount;
    }
}
