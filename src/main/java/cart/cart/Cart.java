package cart.cart;

import cart.cartitem.CartItem;
import cart.deliveryprice.DeliveryPrice;

import java.util.List;

public class Cart {
    private final List<CartItem> cartItems;
    private int originalDeliveryPrice;
    private int discountDeliveryPrice;
    private int discountFromTotalPrice;

    public Cart(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        this.originalDeliveryPrice = DeliveryPrice.DEFAULT_PRICE;
        this.discountFromTotalPrice = 0;
    }

    public int calculateTotalPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::getDiscountedPrice)
                .sum();
    }

    public int calculateFinalDeliveryPrice() {
        return this.originalDeliveryPrice - this.discountDeliveryPrice;
    }

    public void addDiscountForDeliveryPrice(int discountDeliveryPrice) {
        this.discountDeliveryPrice += discountDeliveryPrice;
    }

    public void addDiscountFromTotalPrice(int discountPrice) {
        this.discountFromTotalPrice += discountPrice;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public int getOriginalDeliveryPrice() {
        return originalDeliveryPrice;
    }

    public int getDiscountDeliveryPrice() {
        return discountDeliveryPrice;
    }

    public int getDiscountFromTotalPrice() {
        return discountFromTotalPrice;
    }
}
