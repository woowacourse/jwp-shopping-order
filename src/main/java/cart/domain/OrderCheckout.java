package cart.domain;

import java.util.List;

public class OrderCheckout {

    private static final double POINT_CHARGE_RATE = 0.1;
    private static final double POINT_SPEND_RATE = 0.1;
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

    public static OrderCheckout generate(final int currentPoints, final List<CartItem> cartItems) {
        final int totalPrice = calculateTotalPrice(cartItems);
        final int earnedPoints = (int) calculateEarnedPoints(totalPrice);
        final int limitPoints = (int) calculateLimitPoints(totalPrice);
        final int availablePoints = Math.min(currentPoints, limitPoints);

        return new OrderCheckout(cartItems, totalPrice, currentPoints, earnedPoints, availablePoints);
    }

    private static int calculateTotalPrice(final List<CartItem> cartItems) {
        return cartItems.stream().map(cartItem ->
                        cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .mapToInt(i -> i)
                .sum();
    }

    private static double calculateEarnedPoints(final int totalPrice) {
        return totalPrice * POINT_CHARGE_RATE;
    }

    private static double calculateLimitPoints(final int totalPrice) {
        return totalPrice * POINT_SPEND_RATE;
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
