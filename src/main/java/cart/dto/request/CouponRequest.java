package cart.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CouponRequest {

    @NotNull(message = "name 필드가 필요합니다.")
    private final String name;

    @NotNull(message = "type 필드가 필요합니다.")
    private final String type;

    @NotNull(message = "amount 필드가 필요합니다.")
    @Positive(message = "쿠폰의 할인양은 양수여야 합니다.")
    private final Integer amount;

    public CouponRequest(final String name, final String type, final int amount) {
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
