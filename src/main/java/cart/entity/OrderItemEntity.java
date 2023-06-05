package cart.entity;

import cart.domain.OrderItem;

public class OrderItemEntity {
    private final Long id;
    private final String name;
    private final long price;
    private final String imageUrl;
    private final Integer quantity;
    private final Long orderId;

    public OrderItemEntity(final String name, final long price, final String imageUrl, final Integer quantity,
                           final Long orderId) {
        this(null, name, price, imageUrl, quantity, orderId);
    }

    public OrderItemEntity(final Long id, final String name, final long price, final String imageUrl,
                           final int quantity, final Long orderId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public static OrderItemEntity ofOrderItemAndOrderId(final OrderItem orderItem, final Long orderId) {
        return new OrderItemEntity(orderItem.getName(), orderItem.getPrice(), orderItem.getImageUrl(),
                orderItem.getQuantity(), orderId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getOrderId() {
        return orderId;
    }
}
