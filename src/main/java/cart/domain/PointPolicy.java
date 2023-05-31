package cart.domain;


import java.util.List;
import java.util.function.ToIntFunction;

public class PointPolicy {
    private static final int TOTAL_PORTION = 100;
    private static final int SAVING_RATE = 10;

    private PointPolicy() {
    }

    public static int calculateSavingPoints(final int usedPoints, final List<CartItem> cartItems) {
        final int totalPrice = cartItems.stream()
                .mapToInt(calculateTotalItemPrice())
                .sum();
        final int usedMoney = totalPrice - usedPoints;

        return usedMoney * SAVING_RATE / TOTAL_PORTION;
    }

    private static ToIntFunction<CartItem> calculateTotalItemPrice() {
        return cartItem -> cartItem.getQuantity() * cartItem.getProduct().getPrice();
    }

    public static int getSavingRate() {
        return SAVING_RATE;
    }
}
