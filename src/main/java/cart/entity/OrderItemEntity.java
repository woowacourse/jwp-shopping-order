package cart.entity;

import cart.domain.Product;

import java.util.Objects;

public class OrderItemEntity {

    private Long orderId;
    private Product product;
    private int quantity;
    private int totalPrice;

    public OrderItemEntity(Long orderId, Product product, int quantity, int totalPrice) {
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public OrderItemEntity(Product product, int quantity, int totalPrice) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
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
        return quantity == that.quantity && totalPrice == that.totalPrice && Objects.equals(orderId, that.orderId) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, product, quantity, totalPrice);
    }

    @Override
    public String toString() {
        return "OrderItemEntity{" +
                "orderId=" + orderId +
                ", product=" + product +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
