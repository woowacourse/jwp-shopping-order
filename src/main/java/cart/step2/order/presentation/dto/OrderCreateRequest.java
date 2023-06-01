package cart.step2.order.presentation.dto;

import cart.step2.order.domain.Order;

import java.util.List;

public class OrderCreateRequest {

    private List<Long> cartItemIds;
    private Integer price;
    private Long couponId;

    public Order toDomain(final Long memberId) {
        return Order.createNonePkOrder(price, couponId, memberId);
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getCouponId() {
        return couponId;
    }

}
