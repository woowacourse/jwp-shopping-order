package cart.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class PaymentRequest {
    @NotEmpty(message = "장바구니가 비어있습니다.")
    private final List<CartItemRequest> cartItemRequests;

    @PositiveOrZero(message = "포인트는 0원 이상 사용 가능합니다.")
    private final Integer point;

    public PaymentRequest(final List<CartItemRequest> cartItemRequests, final Integer point) {
        this.cartItemRequests = cartItemRequests;
        this.point = point;
    }

    public List<CartItemRequest> getCartItemRequests() {
        return cartItemRequests;
    }

    public Integer getPoint() {
        return point;
    }
}
