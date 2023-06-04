package cart.domain;

import java.sql.Timestamp;

public class Order {

    private final Long id;
    private final CartItems cartItems;
    private final OrderPoint orderPoint;
    private final Timestamp createdAt;

    private Order(final Long id, final CartItems cartItems, final OrderPoint orderPoint, final Timestamp createdAt) {
        this.id = id;
        this.cartItems = cartItems;
        this.orderPoint = orderPoint;
        this.createdAt = createdAt;
    }

    public static Order of(final Long id, final Order other) {
        return new Order(id, other.cartItems, other.orderPoint, other.createdAt);
    }

    public static Order of(final CartItems cartItems, final OrderPoint orderPoint, final Timestamp createdAt) {
        return new Order(null, cartItems, orderPoint, createdAt);
    }

    public static Order of(final Long id, final CartItems cartItems, final OrderPoint orderPoint, final Timestamp createdAt) {
        return new Order(id, cartItems, orderPoint, createdAt);
    }

    public Long getId() {
        return id;
    }

    public CartItems getCartItems() {
        return cartItems;
    }

    public OrderPoint getOrderPoint() {
        return orderPoint;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
