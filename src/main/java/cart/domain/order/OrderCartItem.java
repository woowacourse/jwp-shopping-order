package cart.domain.order;

import cart.domain.CartItem;
import cart.domain.value.Price;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.ProductCoupon;

public class OrderCartItem {
    private final CartItem cartItem;
    private final Coupon coupon;

    public OrderCartItem(CartItem cartItem, Coupon coupon) {
        this.cartItem = cartItem;
        if (coupon != null && !coupon.getCategory().equals(ProductCoupon.CATEGORY)) {
            throw new IllegalArgumentException("지원하지 않는 쿠폰입니다.");
        }
        this.coupon = coupon;
    }

    public OrderCartItem(CartItem cartItem) {
        this(cartItem, null);
    }

    public Price getOriginalPrice() {
        return cartItem.getPrice();
    }

    public Price getDiscountedPrice() {
        if (coupon == null) {
            return cartItem.getDiscountedPrice();
        }
        return cartItem.applyCoupon(coupon);
    }

    public Price getDiscountPrice() {
        return getOriginalPrice().minus(getDiscountedPrice());
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public Coupon getCoupon() {
        return coupon;
    }
}
