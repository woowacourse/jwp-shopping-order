package cart.domain;

import cart.exception.IllegalPointUsageException;

import java.util.List;
import java.util.Objects;

public class Order {

    private static final int MINIMUM_UNIT_POINTS = 10;

    private final Long id;
    private final int usedPoints;
    private final List<CartItem> cartItems;

    public static Order of(final int points, final List<CartItem> cartItems) {
        return new Order(null, points, cartItems);
    }

    private Order(final Long id, final int usedPoints, final List<CartItem> cartItems) {
        this.id = id;
        this.usedPoints = usedPoints;
        this.cartItems = cartItems;
    }

    public void validatePoints(final int memberPoints) {
        if (usedPoints > memberPoints) {
            throw new IllegalPointUsageException("사용자가 가진 포인트보다 많은 포인트 사용을 시도했습니다.");
        }
        if (usedPoints % MINIMUM_UNIT_POINTS != 0) {
            throw new IllegalPointUsageException(String.format("포인트 사용 단위는 최소 %d원 입니다.", MINIMUM_UNIT_POINTS));
        }
    }

    public Long getId() {
        return id;
    }

    public int getUsedPoints() {
        return usedPoints;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order other = (Order) o;
        if (id == null || other.id == null) {
            return false;
        }
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
