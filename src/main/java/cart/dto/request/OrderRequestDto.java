package cart.dto.request;

import java.util.List;

public class OrderRequestDto {
    private List<Integer> cartItems;
    private int totalPrice;
    private int couponId;

    public OrderRequestDto() {
    }

    public OrderRequestDto(final List<Integer> cartItems, final int totalPrice, final int couponId) {
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
        this.couponId = couponId;
    }

    public List<Integer> getCartItems() {
        return cartItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getCouponId() {
        return couponId;
    }
}
