package cart.cart;

import cart.cart.domain.cartitem.CartItem;
import cart.coupon.Coupon;

import java.util.List;

public class Cart {
    public static final int DEFAULT_DELIVERY_PRICE = 3000;
    public static final int FREE_DELIVERY_PRICE_LIMIT = 30000;
    private final List<CartItem> cartItems;
    private int deliveryPrice;
    private final List<Coupon> coupons;

    public Cart(List<CartItem> cartItems, List<Coupon> coupons) {
        this.cartItems = cartItems;
        this.deliveryPrice = DEFAULT_DELIVERY_PRICE;
        this.coupons = coupons;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setDeliveryPrice(int deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
}
