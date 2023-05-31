package cart.entity;

import cart.domain.Money;
import cart.domain.order.OrderItem;

public class OrderItemEntity {

    private final Long id;
    private final String name;
    private final String imageUrl;
    private final long price;
    private final Integer quantity;
    private final Long orderId;

    public OrderItemEntity(final Long id, final String name, final String imageUrl, final long price, final Integer quantity, final Long orderId) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public OrderItemEntity(final String name, final String imageUrl, final long price, final Integer quantity, final Long orderId) {
        this(null, name, imageUrl, price, quantity, orderId);
    }

    public static OrderItemEntity of(final OrderItem orderItem, final Long orderId) {
        return new OrderItemEntity(
                orderItem.getId(),
                orderItem.getName(),
                orderItem.getImageUrl(),
                orderItem.getPrice().getValue(),
                orderItem.getQuantity(),
                orderId
        );
    }

    public OrderItem toDomain() {
        return new OrderItem(id, name, imageUrl, new Money(price), quantity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getOrderId() {
        return orderId;
    }
}
