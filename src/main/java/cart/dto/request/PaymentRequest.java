package cart.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public class PaymentRequest {

    @Size(min = 1, message = "결제하려는 상품은 하나 이상이어야 합니다.")
    @NotNull(message = "결제하려는 상품이 입력되지 않았습니다.")
    private List<CartItemIdRequest> cartItemIds;
    @PositiveOrZero(message = "상품 금액의 합은 0보다 커야합니다.")
    private Integer originalPrice;
    @PositiveOrZero(message = "사용하려는 포인트는 0이상이어야 합니다.")
    private Integer points;

    public PaymentRequest() {
    }

    public PaymentRequest(final List<CartItemIdRequest> cartItemIds, final Integer originalPrice,
                          final Integer points) {
        this.cartItemIds = cartItemIds;
        this.originalPrice = originalPrice;
        this.points = points;
    }

    public List<CartItemIdRequest> getCartItemIds() {
        return cartItemIds;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public Integer getPoints() {
        return points;
    }
}
