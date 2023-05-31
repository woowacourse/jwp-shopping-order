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

    public Order(final int points, final List<CartItem> cartItems) {
        this(null, points, cartItems);
    }

    public void validatePoints(final int memberPoints) {
        if (points > memberPoints) {
            throw new IllegalPointUsageException("사용자가 가진 포인트보다 많은 포인트 사용을 시도했습니다.");
        }
        if (points % MINIMUM_UNIT_POINTS != 0) {
            throw new IllegalPointUsageException(String.format("포인트 사용 단위는 최소 %d원 입니다.", MINIMUM_UNIT_POINTS));
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
