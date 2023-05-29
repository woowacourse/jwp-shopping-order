package cart.domain;

import java.util.List;

public class Order {

    private final List<CartItem> cartItems;
    private final int originalPrice;
    private final int usedPoint;
    private final int pointToAdd;

    public Order(List<CartItem> cartItems, int originalPrice, int usedPoint, int pointToAdd) {
        this.cartItems = cartItems;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getPointToAdd() {
        return pointToAdd;
    }
}
