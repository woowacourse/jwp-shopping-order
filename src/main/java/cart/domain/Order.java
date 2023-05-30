package cart.domain;

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

    public Order(final int points, final List<CartItem> cartItems) {
        this(null, points, cartItems);
    }

    public void validatePoints(final int memberPoints) {
        if (points > memberPoints) {
            throw new IllegalArgumentException("보유한 포인트보다 많이 사용할 수 없습니다.");
        }
        if (points % MINIMUM_UNIT_POINTS != 0) {
            throw new IllegalArgumentException(String.format("포인트는 %d 단위로 사용할 수 있습니다.", MINIMUM_UNIT_POINTS));
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
