package cart.dto.request;

import javax.validation.constraints.NotNull;
import java.util.List;

public class PayRequest {
    @NotNull(message = "구매할 카트 아이템의 아이디를 입력해 주세요. 입력값 : ${validatedValue}")
    private final List<Long> cartItemIds;

    @NotNull(message = "선택한 아이템의 정가를 입력해 주세요. 입력값 : ${validatedValue}")
    private final Integer originalPrice;

    @NotNull(message = "사용할 포인트를 입력해 주세요. 입력값 : ${validatedValue}")
    private final int points;

    public PayRequest(final List<Long> cartItemIds, final int originalPrice, final Integer points) {
        this.cartItemIds = cartItemIds;
        this.originalPrice = originalPrice;
        this.points = points;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public Integer getPoints() {
        return points;
    }
}
