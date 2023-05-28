package cart.dto.sale;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class SaleProductRequest {

    @Min(value = 1, message = "최소 할인은 1% 입니다.")
    @Max(value = 100, message = "최대 할인은 100% 입니다.")
    private int amount;

    public SaleProductRequest() {

    }

    public int getAmount() {
        return amount;
    }
}
