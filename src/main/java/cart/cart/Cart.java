package cart.cart;

import cart.cart.domain.cartitem.CartItem;
import cart.cart.domain.deliveryprice.DeliveryPrice;

import java.util.List;

public class Cart {
    private final List<CartItem> cartItems;
    private DeliveryPrice deliveryPrice;
    private int discountFromTotalPrice;

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

    public int getDiscountFromTotalPrice() {
        return discountFromTotalPrice;
    }

    public void setDiscountFromTotalPrice(int discountFromTotalPrice) {
        this.discountFromTotalPrice = discountFromTotalPrice;
    }

    public void setDeliveryPrice(DeliveryPrice deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int calculateProductsTotalPrice() {
        return this.cartItems
                .stream().mapToInt(CartItem::getDiscountedPrice)
                .sum();
    }
}
