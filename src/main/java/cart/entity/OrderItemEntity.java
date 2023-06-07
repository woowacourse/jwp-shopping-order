package cart.entity;

import java.util.Objects;

public class OrderItemEntity {

    private Long orderId;
    private final Long productId;
    private final int quantity;
    private final int totalPrice;

    public OrderItemEntity(Long orderId, Long productId, int quantity, int totalPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public OrderItemEntity(Long productId, int quantity, int totalPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntity that = (OrderItemEntity) o;
        return quantity == that.quantity && totalPrice == that.totalPrice && Objects.equals(orderId, that.orderId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId, quantity, totalPrice);
    }

    @Override
    public String toString() {
        return "OrderItemEntity{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
