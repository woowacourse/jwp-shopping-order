package cart.dao.entity;

import cart.domain.Money;
import cart.domain.OrderItem;

public class OrderItemEntity {
    private final Long id;
    private final String name;
    private final int quantity;
    private final String imageUrl;
    private final int totalPrice;
    private final long orderId;

    public OrderItemEntity(final Long id, final String name, final int quantity, final String imageUrl, final int totalPrice, final long orderId) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderId = orderId;
    }

    public static OrderItemEntity from(final OrderItem orderItem, final long orderId) {
        return new OrderItemEntity(null, orderItem.getName(), orderItem.getQuantity(), orderItem.getImageUrl(), orderItem.getTotalPrice().getValue(), orderId);
    }

    public long getOrderId() {
        return this.orderId;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public int getTotalPrice() {
        return this.totalPrice;
    }

    public OrderItem toOrderItem() {
        return new OrderItem(this.name, this.quantity, this.imageUrl, Money.from(this.totalPrice));
    }
}
