package cart.dto;

import cart.domain.OrderItem;

import java.util.Objects;

public class OrderItemResponse {

    private final Long id;
    private final int quantity;
    private final ProductResponse product;

    private OrderItemResponse(Long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static OrderItemResponse of(OrderItem orderItem) {
        return new OrderItemResponse(orderItem.getId(), orderItem.getQuantity(),
                ProductResponse.of(orderItem.getProduct()));
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItemResponse that = (OrderItemResponse) o;
        return quantity == that.quantity && Objects.equals(id, that.id) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, product);
    }

    @Override
    public String toString() {
        return "OrderItemResponse{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", product=" + product +
                '}';
    }
}
