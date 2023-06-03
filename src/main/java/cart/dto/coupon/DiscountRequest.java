package cart.dto.coupon;

import cart.domain.coupon.Discount;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class DiscountRequest {

    @NotBlank(message = "쿠폰 종류는 필수 입력입니다. 반드시 입력해주세요.")
    private String type;
    @Min(value = 0, message = "쿠폰 할인률은 입력입니다. 반드시 입력해주세요.")
    private int amount;

    private DiscountRequest() {
    }

    public DiscountRequest(final String type, final int amount) {
        this.type = type;
        this.amount = amount;
    }

    public DiscountRequest(final Discount discount) {
        this(discount.getDiscountType().name(), discount.getAmount());
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }
}
