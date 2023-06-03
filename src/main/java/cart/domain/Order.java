package cart.domain;

import cart.exception.IllegalPointUsageException;

import java.util.List;

public class Order {
    private static final int MINIMUM_UNIT_POINTS = 10;

    private final Long id;
    private final int points;
    private final List<CartItem> cartItems;

    public Order(final Long id, final int points, final List<CartItem> cartItems) {
        this.id = id;
        this.points = points;
        this.cartItems = cartItems;
    }

    public Order(final int usedPoints, final List<CartItem> cartItems, final int memberPoints) {
        this(null, usedPoints, cartItems);
        validatePoints(memberPoints);
    }

    private void validatePoints(final int memberPoints) {
        if (points > memberPoints) {
            throw new IllegalPointUsageException();
        }
        if (points % MINIMUM_UNIT_POINTS != 0) {
            throw new IllegalPointUsageException();
        }
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
