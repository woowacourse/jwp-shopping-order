package cart.dto.request;

import java.util.List;

public class OrderRequestDto {
    private List<Long> cartItemIds;
    private int totalPrice;
    private Long couponId;

    public OrderRequestDto() {
    }

    public OrderRequestDto(final List<Long> cartItemIds, final int totalPrice, final Long couponId) {
        this.cartItemIds = cartItemIds;
        this.totalPrice = totalPrice;
        this.couponId = couponId;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public Long getCouponId() {
        return couponId;
    }
}
