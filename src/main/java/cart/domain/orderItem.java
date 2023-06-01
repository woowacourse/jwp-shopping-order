package cart.domain;

import cart.domain.coupon.Coupon;

public class orderItem {
    private final CartItem cartItem;
    private final Coupon coupon;

    public orderItem(CartItem cartItem, Coupon coupon) {
        this.cartItem = cartItem;
        this.coupon = coupon;
    }

    public Price getPrice() {
        return cartItem.getProduct().getPrice();
    }
}
