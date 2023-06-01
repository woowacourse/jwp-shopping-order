package cart.dao.entity;

import cart.domain.OrderItem;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderItemEntity {

    private final Long id;
    private final long orderId;
    private final String name;
    private final long price;
    private final String imageUrl;
    private final int quantity;

    public OrderItemEntity(final Long id,
                           final long orderId,
                           final String name,
                           final long price,
                           final String imageUrl,
                           final int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public OrderItemEntity(final long orderId,
                           final String name,
                           final long price,
                           final String imageUrl,
                           final int quantity) {
        this(null, orderId, name, price, imageUrl, quantity);
    }

    public static OrderItemEntity of(final long orderId, final OrderItem orderItem) {
        return new OrderItemEntity(
                orderId,
                orderItem.getName(),
                orderItem.getPrice(),
                orderItem.getImageUrl(),
                orderItem.getQuantity());
    }

    public static List<OrderItemEntity> of(final long orderId, final List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> OrderItemEntity.of(orderId, orderItem))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
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

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderItemEntity that = (OrderItemEntity) o;
        return orderId == that.orderId && price == that.price && quantity == that.quantity && Objects.equals(name,
                that.name) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, name, price, imageUrl, quantity, id);
    }

    @Override
    public String toString() {
        return "OrderItemEntity{" +
                "orderId=" + orderId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", quantity=" + quantity +
                ", id=" + id +
                '}';
    }
}
