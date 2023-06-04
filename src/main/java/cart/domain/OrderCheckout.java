package cart.domain;

import cart.domain.pointmanager.PointManager;

import java.util.List;

public class OrderCheckout {

    private final List<CartItem> cartItems;
    private final int totalPrice;
    private final int currentPoints;
    private final int earnedPoints;
    private final int availablePoints;

    private OrderCheckout(final List<CartItem> cartItems, final int totalPrice, final int currentPoints, final int earnedPoints, final int availablePoints) {
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
        this.currentPoints = currentPoints;
        this.earnedPoints = earnedPoints;
        this.availablePoints = availablePoints;
    }

    public static OrderCheckout of(final int currentPoints, final List<CartItem> cartItems, final PointManager pointManager) {
        final int totalPrice = calculateTotalPrice(cartItems);
        final int earnedPoints = pointManager.calculateEarnedPoints(totalPrice);
        final int limitPoints = pointManager.calculateLimitPoints(totalPrice);
        final int availablePoints = pointManager.calculateAvailablePoints(currentPoints, limitPoints);

        return new OrderCheckout(cartItems, totalPrice, currentPoints, earnedPoints, availablePoints);
    }

    private static int calculateTotalPrice(final List<CartItem> cartItems) {
        return cartItems.stream().map(cartItem ->
                        cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .mapToInt(i -> i)
                .sum();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public int getAvailablePoints() {
        return availablePoints;
    }
}
