package cart.dao.entity;

import cart.domain.OrderItem;
import cart.domain.Product;

import java.util.Objects;

public class OrderItemEntity {
    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final Integer quantity;

    public OrderItemEntity(final Long id, final Long orderId, final Long productId, final String name, final Integer price, final String imageUrl, final Integer quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static OrderItemEntity of(final OrderItem orderItem, final Long savedOrderId) {
        Product product = orderItem.getProduct();
        return new OrderItemEntity(
                null,
                savedOrderId,
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                orderItem.getQuantity()
        );
    }

    // TODO: 5/31/23 new Product 말고 다른 방법 생각해보기 
    public OrderItem toOrderItem() {
        return new OrderItem(
                id,
                new Product(productId, name, price, imageUrl),
                quantity
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderItemEntity that = (OrderItemEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(orderId, that.orderId) && Objects.equals(productId, that.productId) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, productId, name, price, imageUrl, quantity);
    }

    @Override
    public String toString() {
        return "OrderItemEntity{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
