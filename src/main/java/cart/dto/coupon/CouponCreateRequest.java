package cart.dto.coupon;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CouponCreateRequest {

    @NotBlank(message = "쿠폰의 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "할인 정책이 정률인지 정액인지 입력해주세요.")
    private Boolean isPercentage;

    @NotNull(message = "정률이라면 %를 입력해주시고, 아니라면 할인할 가격을 입력해주세요.")
    private Integer amount;

    public CouponCreateRequest() {

    }

    public CouponCreateRequest(final String name, final Boolean isPercentage, final int amount) {
        this.name = name;
        this.isPercentage = isPercentage;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsPercentage() {
        return isPercentage;
    }

    public int getAmount() {
        return amount;
    }
}
