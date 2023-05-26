package cart.order.application.dto;

import java.util.List;

public class PlaceOrderCommand {

    private final Long memberId;
    private final List<Long> cartItemIds;

    public PlaceOrderCommand(Long memberId, List<Long> cartItemIds) {
        this.memberId = memberId;
        this.cartItemIds = cartItemIds;
    }

    public Long getMemberId() {
        return memberId;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}
