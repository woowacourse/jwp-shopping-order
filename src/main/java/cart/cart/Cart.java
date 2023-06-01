package cart.cart;

import cart.cart.domain.cartitem.CartItem;
import cart.cart.domain.deliveryprice.DeliveryPrice;
import cart.coupon.Coupon;

import java.util.List;

public class Cart {
    public static final int DEFAULT_DELIVERY_PRICE = 3000;
    public static final int FREE_DELIVERY_PRICE_LIMIT = 30000;
    private final List<CartItem> cartItems;
    private DeliveryPrice deliveryPrice;

    public Cart(List<CartItem> cartItems, DeliveryPrice deliveryPrice) {
        this.cartItems = cartItems;
        this.deliveryPrice = deliveryPrice;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public DeliveryPrice getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(DeliveryPrice deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
}
