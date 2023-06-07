package cart.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DiscountRequest {

    @NotBlank(message = "쿠폰 타입을 입력해주세요.")
    private String type;

    @NotNull(message = "할인 금액을 입력해주세요.")
    private Integer amount;

    public DiscountRequest() {
    }

    public DiscountRequest(String type, Integer amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public Integer getAmount() {
        return amount;
    }
}
