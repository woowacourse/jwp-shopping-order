package cart.dto.coupon;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class MemberCouponRequest {

    @NotNull(message = "ID는 필수 입력입니다. 반드시 입력해주세요.")
    @Positive(message = "유효한 ID를 입력해주세요.")
    @JsonProperty("couponId")
    private Long id;
    @NotBlank(message = "이름은 필수 입력입니다. 반드시 입력해주세요.")
    private String name;
    @JsonProperty("discount")
    private DiscountRequest discountRequest;

    private MemberCouponRequest() {
    }

    public MemberCouponRequest(final Long id, final String name, final DiscountRequest discountRequest) {
        this.id = id;
        this.name = name;
        this.discountRequest = discountRequest;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DiscountRequest getDiscountRequest() {
        return discountRequest;
    }
}
