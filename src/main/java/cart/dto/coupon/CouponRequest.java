package cart.dto.coupon;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CouponRequest {

    @NotBlank(message = "이름은 필수 입력입니다. 반드시 입력해주세요.")
    private String name;
    @NotBlank(message = "쿠폰 종류는 필수 입력입니다. 반드시 입력해주세요.")
    private String type;
    @Min(value = 0, message = "쿠폰 할인율은 음수일수 없습니다.")
    @NotNull(message = "쿠폰 할인률은 필수 입력입니다. 반드시 입력해주세요.")
    private Integer amount;

    private CouponRequest() {
    }

    public CouponRequest(final String name, final String type, final Integer amount) {
        this.name = name;
        this.type = type;
        this.amount = amount;
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
