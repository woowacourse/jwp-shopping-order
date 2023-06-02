package cart.dto.coupon;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class MemberCouponRequest {

    @NotNull(message = "ID는 필수 입력입니다. 반드시 입력해주세요.")
    @Positive(message = "유효한 ID를 입력해주세요.")
    private Long id;
    @NotBlank(message = "이름은 필수 입력입니다. 반드시 입력해주세요.")
    private String name;
    @NotBlank(message = "쿠폰 종류는 필수 입력입니다. 반드시 입력해주세요.")
    private String type;
    @Min(value = 0, message = "쿠폰 할인률은 입력입니다. 반드시 입력해주세요.")
    private int amount;

    private MemberCouponRequest() {
    }

    public MemberCouponRequest(final Long id, final String name, final String type, final int amount) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.amount = amount;
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

    public int getAmount() {
        return amount;
    }
}
