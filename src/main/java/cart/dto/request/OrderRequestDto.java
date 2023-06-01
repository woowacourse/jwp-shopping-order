package cart.dto.request;

import java.util.List;

public class OrderRequestDto {
    private List<Long> cartItems;
    private int totalPrice;
    private Long couponId;

    public OrderRequestDto() {
    }

    public OrderRequestDto(final List<Long> cartItems, final int totalPrice, final Long couponId) {
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
        this.couponId = couponId;
    }

    public List<Long> getCartItems() {
        return cartItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public Long getCouponId() {
        return couponId;
    }
}
