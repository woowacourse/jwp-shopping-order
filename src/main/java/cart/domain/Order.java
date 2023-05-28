package cart.domain;

import java.util.List;

public class Order {
    private static final int savingRate = 10;
    private static final int TOTAL_PORTION = 100;
    private static final int MINIMUM_UNIT_POINTS = 10;

    private final Long id;
    private final int points;
    private final List<CartItem> cartItems;

    public Order(final Long id, final int points, final List<CartItem> cartItems) {
        this.id = id;
        this.points = points;
        this.cartItems = cartItems;
    }

    public Order(final int points, final List<CartItem> cartItems) {
        this(null, points, cartItems);
    }

    public int calculateSavingPoints(final int usedPoints) {
        final int totalPrice = cartItems.stream()
                .map(CartItem::getProduct)
                .mapToInt(Product::getPrice)
                .sum();
        final int usedMoney = totalPrice - usedPoints;

        return usedMoney * savingRate / TOTAL_PORTION;
    }

    public void validatePoints(final int memberPoints) {
        if (points > memberPoints) {
            throw new IllegalArgumentException();
        }
        if (points % MINIMUM_UNIT_POINTS != 0) {
            throw new IllegalArgumentException();
        }
    }

    public int getSavingRate() {
        return savingRate;
    }

    public Long getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
